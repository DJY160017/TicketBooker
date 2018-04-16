package booker.task;

import booker.model.BankAccount;
import booker.model.Order;
import booker.model.Ticket;
import booker.model.id.ProgramID;
import booker.service.ExternalBalanceService;
import booker.service.OrderService;
import booker.service.TicketService;
import booker.util.enums.state.OrderState;
import booker.util.enums.state.TicketState;
import booker.util.exception.BalanceInsufficientException;
import booker.util.exception.PasswordInvalidException;
import booker.util.exception.UserNotExistException;
import booker.util.infoCarrier.ManagerAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AllocationTask {

    private final OrderService orderService;

    private final TicketService ticketService;

    private final ExternalBalanceService externalBalanceService;

    @Autowired
    public AllocationTask(OrderService orderService, TicketService ticketService, ExternalBalanceService externalBalanceService) {
        this.orderService = orderService;
        this.ticketService = ticketService;
        this.externalBalanceService = externalBalanceService;
    }

    // 凌晨4点自动更新
    @Scheduled(cron = "0 0 4 * * ?")
    public void allocate() {
        List<Order> orders = orderService.getAutoTicketOrders();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (isTwoWeeks(order.getOrderID().getOrderTime())) { //演出前两周进行配票
                String detail = order.getDetail();
                String program_detail[] = detail.split(";");
                ProgramID programID = new ProgramID();
                programID.setVenueID(Integer.parseInt(program_detail[0])); //订单详情的规则
                programID.setReserve_time(LocalDateTime.parse(program_detail[1]));
                List<Ticket> tickets = ticketService.getProgramTicket(programID, TicketState.PendingReservation);
                int num = Integer.parseInt(program_detail[2]);
                if (tickets.size() < num) { // 退款
                    BankAccount bankAccount = externalBalanceService.getUserAccount(order.getOrderID().getUserID());
                    try {
                        externalBalanceService.transferAccounts(ManagerAccount.getAccount(), ManagerAccount.getAccountPassword(), bankAccount.getAccount(), order.getTotal_price());
                        order.setOrderState(OrderState.Invalid);
                        order.setDetail(order.getDetail() + ";配票失败,钱已退回您的账户");
                        orderService.updateOrder(order);
                        continue;
                    } catch (UserNotExistException | PasswordInvalidException | BalanceInsufficientException e) {
                        e.printStackTrace();
                    }
                }

                List<Ticket> need_tickets = new ArrayList<>();
                StringBuilder builder = new StringBuilder();
                builder.append(programID.getVenueID()).append("-").append(programID.getReserve_time()).append(";");
                for (int j = 0; j < tickets.size(); j++) { // 处理每一张票和订单
                    if (j > num) {
                        break;
                    }
                    Ticket ticket = tickets.get(j);
                    builder.append(ticket.getTicketID().getRaw_num()).append("排")
                            .append(ticket.getTicketID().getCol_num()).append("座")
                            .append("-").append(ticket.getPrice()).append(";");
                    ticket.setTicketState(TicketState.AlreadyPaid);
                    ticketService.updateTicketState(ticket.getTicketID(), TicketState.AlreadyPaid);
                    need_tickets.add(ticket);
                }
                order.setTickets(need_tickets);
                String order_detail = builder.toString();
                order.setDetail(order_detail.substring(0, order_detail.length() - 1));
                orderService.updateOrder(order);
            }
        }
    }

    /**
     * 判断当前日期是否为演出前两周
     *
     * @param localDateTime 演出时间
     * @return boolean
     */
    private boolean isTwoWeeks(LocalDateTime localDateTime) {
        LocalDate now = LocalDate.now();
        LocalDate needTime = localDateTime.toLocalDate();
        Duration duration = Duration.between(needTime, now);
        return duration.toDays() == 14;
    }
}
