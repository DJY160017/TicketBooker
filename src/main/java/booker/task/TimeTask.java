package booker.task;

import booker.model.Order;
import booker.model.Program;
import booker.model.Settlement;
import booker.model.Ticket;
import booker.model.id.TicketID;
import booker.service.OrderService;
import booker.service.ProgramService;
import booker.service.SettlementService;
import booker.service.TicketService;
import booker.util.enums.state.OrderState;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.SettlementState;
import booker.util.enums.state.TicketState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeTask {

    private final OrderService orderService;

    private final TicketService ticketService;

    private final ProgramService programService;

    private final SettlementService settlementService;

    @Autowired
    public TimeTask(OrderService orderService, TicketService ticketService, ProgramService programService, SettlementService settlementService) {
        this.orderService = orderService;
        this.ticketService = ticketService;
        this.programService = programService;
        this.settlementService = settlementService;
    }

    // 每分钟自动更新
    @Scheduled(cron = "0 0/1 * * * ?")
    public void countDown() {
        List<Order> orders = orderService.getOrderByState(OrderState.Unpaid);
        LocalDateTime now = LocalDateTime.now();
        for (Order order : orders) {
            Duration duration = Duration.between(order.getOrderID().getOrderTime(), now);
            if (duration.toMinutes() > 5) {
                List<TicketID> ticketIDS = new ArrayList<>();
                for (Ticket ticket : order.getTickets()) {
                    ticketIDS.add(ticket.getTicketID());
                }
                orderService.updateOrderState(order.getOrderID(), OrderState.Invalid);
                ticketService.updateTicketsState(ticketIDS, TicketState.PendingReservation);
            }
        }

        //更新节目状态
        List<Program> programs = programService.getPlanByState(ProgramState.AlreadyPassed);
        for (Program program : programs) {
            LocalDateTime need = program.getProgramID().getReserve_time();
            if (need.isBefore(now)) {
                programService.updatePlanState(program.getProgramID(), ProgramState.Invalid);
            }
        }
    }

    // 凌晨5点自动更新
    @Scheduled(cron = "0 0 5 * * ?")
    public void systemStateUpdate() { //更新结算状态（未结算），订单状态（已失效）
        LocalDate now = LocalDate.now();
        //更新结算状态
        List<Settlement> settlements = settlementService.getSettlement(SettlementState.Unfinished);
        for (Settlement settlement : settlements) {
            LocalDate need = settlement.getProgramID().getReserve_time().toLocalDate();
            if (need.isBefore(now)) {
                settlementService.updateSettlementState(settlement.getSettlementID(), SettlementState.Unsettled);
            }
        }

        //更新订单状态
        List<Order> orders = orderService.getOrderByState(OrderState.AlreadyPaid);
        for (Order order : orders) {
            LocalDate need = order.getProgramID().getReserve_time().toLocalDate();
            if (need.isBefore(now)) {
                orderService.updateOrderState(order.getOrderID(), OrderState.Invalid);
            }
        }
        //更新票的状态
    }
}
