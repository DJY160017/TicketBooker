package booker.controller;

import booker.model.*;
import booker.model.id.OrderID;
import booker.model.id.SettlementID;
import booker.service.*;
import booker.util.enums.state.OrderState;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.SettlementState;
import booker.util.enums.state.TicketState;
import booker.util.exception.BalanceInsufficientException;
import booker.util.exception.PasswordInvalidException;
import booker.util.exception.UserNotExistException;
import booker.util.formatter.VenueIDFormatter;
import booker.util.infoCarrier.ManagerAccount;
import booker.util.infoCarrier.TicketInitInfo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/external")
public class ExternalBalanceController {

    private final ExternalBalanceService externalBalanceService;

    private final OrderService orderService;

    private final SettlementService settlementService;

    private final TicketService ticketService;

    private final ProgramService programService;

    private final MemberService memberService;

    @Autowired
    public ExternalBalanceController(ExternalBalanceService externalBalanceService, OrderService orderService, SettlementService settlementService, TicketService ticketService, ProgramService programService, MemberService memberService) {
        this.externalBalanceService = externalBalanceService;
        this.orderService = orderService;
        this.settlementService = settlementService;
        this.ticketService = ticketService;
        this.programService = programService;
        this.memberService = memberService;
    }

    @GetMapping("/payment")
    public String pay() {
        return "external_balance/payment";
    }

    /**
     * 【请求】用户订单支付
     */
    @PostMapping(value = "/req_userPay", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqUserPay(@RequestParam("time") String time, @RequestParam("account") String account,
                      @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userMail = String.valueOf(session.getAttribute("user_mail"));
        JSONObject jsonObject = new JSONObject();
        LocalDateTime localDateTime = formatOrderTime(time);
        OrderID orderID = new OrderID();
        orderID.setUserID(userMail);
        orderID.setOrderTime(localDateTime);
        Order order = orderService.getOrder(orderID);
        try {
            boolean result = externalBalanceService.transferAccounts(account, password, ManagerAccount.getAccount(), order.getTotal_price());
            if (result) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setUserID(userMail);
                bankAccount.setAccount(account);
                externalBalanceService.addBankAccount(bankAccount); //增加用户银行卡记录
                settlementService.updateStoreBalance(order.getProgramID(), order.getTotal_price()); //更新结算记录
                order.setOrderState(OrderState.AlreadyPaid);
                for (int i = 0; i < order.getTickets().size(); i++) {
                    order.getTickets().get(i).setTicketState(TicketState.AlreadyPaid);
                }
                orderService.updateOrder(order); //更新订单状态和票状态
                memberService.addMarks(userMail, (int) order.getTotal_price()); //根据支付金额下取整增加积分
                jsonObject.put("result", "1;支付成功");
            } else {
                jsonObject.put("result", "-1;支付失败");
            }
            return jsonObject.toString();
        } catch (UserNotExistException | PasswordInvalidException | BalanceInsufficientException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】用户订单支付取消
     */
    @PostMapping(value = "/req_cancelUserPay", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqCancelUserPay(@RequestParam("time") String time, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userMail = String.valueOf(session.getAttribute("user_mail"));
        JSONObject jsonObject = new JSONObject();
        LocalDateTime localDateTime = formatOrderTime(time);
        OrderID orderID = new OrderID();
        orderID.setUserID(userMail);
        orderID.setOrderTime(localDateTime);
        orderService.updateOrderState(orderID, OrderState.Unpaid);
        jsonObject.put("result", "1;支付取消成功");
        return jsonObject.toString();
    }

    /**
     * 【请求】预定场馆支付
     */
    @PostMapping(value = "/req_venuePay", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenuePay(@RequestParam("account") String account, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userMail = String.valueOf(session.getAttribute("user_mail"));
        TicketInitInfo ticketInitInfo = (TicketInitInfo) session.getAttribute("ticketInitInfo");
        Program program = ticketInitInfo.getProgram();
        JSONObject jsonObject = new JSONObject();
        try {
            Duration duration = Duration.between(program.getStart_time(), program.getEnd_time());
            double total_price = duration.toDays() * program.getVenue().getPrice();
            boolean result = externalBalanceService.transferAccounts(account, password, ManagerAccount.getAccount(), total_price);
            if (result) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setUserID(userMail);
                bankAccount.setAccount(account);
                externalBalanceService.addBankAccount(bankAccount);
                BankAccount venueAccount = externalBalanceService.getUserAccount(String.valueOf(program.getVenue().getVenueID()));
                SettlementID settlementID = new SettlementID();
                settlementID.setVenueAccount(venueAccount.getAccount());
                settlementID.setStoreAccount(account);
                settlementID.setSettlement_time(LocalDateTime.now());
                Settlement settlement = new Settlement();
                settlement.setSettlementID(settlementID);
                settlement.setStoreTotalPrice(0);
                settlement.setVenueTotalPrice(total_price);
                settlement.setSettlementState(SettlementState.Unfinished);
                settlement.setProgramID(program.getProgramID());
                settlementService.add(settlement);
                program.setProgramState(ProgramState.AlreadyPassed);
                programService.addOneProgram(program);
                ticketService.addTickets(ticketInitInfo);
                jsonObject.put("result", "1;支付成功");
            } else {
                jsonObject.put("result", "-1;支付失败");
            }
            return jsonObject.toString();
        } catch (UserNotExistException | PasswordInvalidException | BalanceInsufficientException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 把纳秒转化秒
     *
     * @param time localDateTime的String形式
     * @return LocalDateTime
     */
    private LocalDateTime formatOrderTime(String time) {
        if (!time.contains(".")) {
            if(!time.contains("T")){
                return LocalDateTime.parse(time.replace(' ', 'T'));
            } else{
                return LocalDateTime.parse(time);
            }
        } else {
            String[] date = time.split("\\.");
            if (!date[0].contains("T")) {
                date[0] = date[0].replace(' ', 'T');
            }
            LocalDateTime result = LocalDateTime.parse(date[0]);
            int first = Integer.parseInt(date[1].substring(0, 1));
            if (first >= 5) {
                result = result.plusSeconds(1);
            }
            return result;
        }
    }
}
