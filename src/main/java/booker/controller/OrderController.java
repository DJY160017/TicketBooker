package booker.controller;

import booker.model.Order;
import booker.model.Program;
import booker.model.Ticket;
import booker.model.id.CouponID;
import booker.model.id.OrderID;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.service.*;
import booker.util.enums.state.OrderState;
import booker.util.enums.state.TicketState;
import booker.util.exception.BalanceInsufficientException;
import booker.util.exception.PasswordInvalidException;
import booker.util.exception.ProgramInvalidException;
import booker.util.exception.UserNotExistException;
import booker.util.formatter.NumberFormatter;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    private final TicketService ticketService;

    private final ProgramService programService;

    private final MemberService memberService;

    private final CouponService couponService;

    @Autowired
    public OrderController(OrderService orderService, TicketService ticketService, ProgramService programService, MemberService memberService, CouponService couponService) {
        this.orderService = orderService;
        this.ticketService = ticketService;
        this.programService = programService;
        this.memberService = memberService;
        this.couponService = couponService;
    }

    @GetMapping("/preview")
    public String preview() {
        return "order/orderPreview";
    }

    /**
     * 【请求】获取用户订单
     */
    @PostMapping(value = "/req_getUserOrder", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetUserOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        List<Order> paidOrder = orderService.getOrderByState(mail, OrderState.AlreadyPaid);
        List<Order> unPaidOrder = orderService.getOrderByState(mail, OrderState.Unpaid);
        List<Order> unSubscribedOrder = orderService.getOrderByState(mail, OrderState.Unsubscribed);
        List<Order> invalidOrder = orderService.getOrderByState(mail, OrderState.Invalid);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paid", paidOrder);
        jsonObject.put("paid_programs", getPrograms(paidOrder));
        jsonObject.put("paid_length", paidOrder.size());
        jsonObject.put("unPaid", unPaidOrder);
        jsonObject.put("unPaid_programs", getPrograms(unPaidOrder));
        jsonObject.put("unPaid_length", unPaidOrder.size());
        jsonObject.put("unSubscribed", unSubscribedOrder);
        jsonObject.put("unSubscribed_programs", getPrograms(unSubscribedOrder));
        jsonObject.put("unSubscribed_length", unSubscribedOrder.size());
        jsonObject.put("invalidOrder", invalidOrder);
        jsonObject.put("invalidOrder_programs", getPrograms(invalidOrder));
        jsonObject.put("invalidOrder_length", invalidOrder.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】获取用户订单详情
     */
    @PostMapping(value = "/req_getOrderDetail", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetOrderDetail(@RequestParam("time") String localDateTime, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        OrderID orderID = new OrderID();
        localDateTime = localDateTime.replace(' ', 'T');
        orderID.setOrderTime(LocalDateTime.parse(localDateTime));
        orderID.setUserID(mail);
        Order order = orderService.getOrder(orderID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isAutoTicket", order.isAutoTicket());
        jsonObject.put("detail", order.getDetail());
        jsonObject.put("total_price", order.getTotal_price());
        return jsonObject.toString();
    }

    /**
     * 【请求】用户订单退订
     */
    @PostMapping(value = "/req_subscribe", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSubscribe(@RequestParam("time") String localDateTime, @RequestParam("account") String account,
                        HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        OrderID orderID = new OrderID();
        localDateTime = localDateTime.replace(' ', 'T');
        orderID.setOrderTime(LocalDateTime.parse(localDateTime));
        orderID.setUserID(mail);
        JSONObject jsonObject = new JSONObject();
        try {
            orderService.unSubscribe(orderID, account);
            jsonObject.put("result", "1;退订成功");
            return jsonObject.toString();
        } catch (ProgramInvalidException | UserNotExistException | BalanceInsufficientException | PasswordInvalidException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】生成订单
     */
    @PostMapping(value = "/req_generateOrder", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGenerateOrder(@RequestParam("total_price") String total_price, @RequestParam("coupons") String coupons,
                            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        Order order = (Order) session.getAttribute("cacheOrder");
        JSONObject jsonObject = new JSONObject();
        try {
            StringBuilder detail = new StringBuilder(order.getDetail());
            double discount = memberService.getDiscount(mail);
            detail.append(";折扣:").append(String.valueOf(discount)).append(";优惠券:");
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, String[].class);
            List<String[]> need_coupons = objectMapper.readValue(coupons, javaType);
            String result = null;
            if (need_coupons.isEmpty()) {
                result = detail.toString();
                result = result.substring(0, result.length() - 5);
            } else {
                for (String[] item : need_coupons) {
                    LocalDateTime time = LocalDateTime.parse(item[0].replace(' ', 'T'));
                    detail.append(time.toString()).append("-").append(item[1]).append("|");
                    CouponID couponID = new CouponID();
                    couponID.setTime(time);
                    couponID.setUserID(mail);
                    couponService.deleteCoupon(couponID);
                }
                result = detail.toString();
                result = result.substring(0, result.length() - 1);
            }
            if (!order.isAutoTicket()) {
                result = result + ";配票成功";
            }
            order.setTotal_price(Double.parseDouble(total_price));
            order.setDetail(result);
            orderService.addOrder(order);
            jsonObject.put("result", "1;订单生成成功");
            jsonObject.put("orderTime", order.getOrderID().getOrderTime());
            return jsonObject.toString();
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】生成订单到session
     */
    @PostMapping(value = "/req_generateOrderToCache", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGenerateOrderToCache(@RequestParam("total_price") String total_price, @RequestParam("detail") String detail,
                                   HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        ProgramID programID = (ProgramID) session.getAttribute("programID");
        Order order = new Order();
        LocalDateTime localDateTime = LocalDateTime.now();
        OrderID orderID = new OrderID();
        orderID.setOrderTime(localDateTime);
        orderID.setUserID(mail);
        order.setOrderID(orderID);
        order.setAutoTicket(false);
        order.setTotal_price(Double.parseDouble(total_price));
        order.setDetail(detail);
        order.setOrderState(OrderState.Unpaid);
        order.setProgramID(programID);
        String ticket_detail[] = detail.split(";");
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 1; i < ticket_detail.length; i++) {
            String seatInfo[] = ticket_detail[i].split("-");
            String raw = seatInfo[0].split("排")[0];
            String col = seatInfo[0].split("排")[1].split("座")[0];
            TicketID ticketID = new TicketID();
            ticketID.setProgramID(programID);
            ticketID.setRaw_num(Integer.parseInt(raw));
            ticketID.setCol_num(Integer.parseInt(col));
            Ticket ticket = ticketService.getOneTicket(ticketID);
            ticket.setTicketState(TicketState.Unpaid);
            ticket.setOrder(order);
            tickets.add(ticket);
        }
        order.setTickets(tickets);
        session.setAttribute("cacheOrder", order);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "1;订单生成成功");
        return jsonObject.toString();
    }

    /**
     * 【请求】生成订单到session
     */
    @PostMapping(value = "/req_generateAutoOrderToCache", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGenerateAutoOrderToCache(@RequestParam("total_price") String total_price, @RequestParam("detail") String detail,
                                       HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        ProgramID programID = (ProgramID) session.getAttribute("programID");
        Order order = new Order();
        OrderID orderID = new OrderID();
        orderID.setOrderTime(LocalDateTime.now());
        orderID.setUserID(mail);
        order.setOrderID(orderID);
        order.setProgramID(programID);
        order.setAutoTicket(true);
        order.setTotal_price(Double.parseDouble(total_price));
        order.setDetail(detail);
        order.setOrderState(OrderState.PendingReservation);
        session.setAttribute("cacheOrder", order);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "1;订单生成成功");
        return jsonObject.toString();
    }

    /**
     * 【请求】获取cache中缓存的order
     */
    @PostMapping(value = "/req_getCacheOrder", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetCacheOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Order order = (Order) session.getAttribute("cacheOrder");
        Program program = programService.getOneProgram(order.getProgramID());
        double discount = memberService.getDiscount(order.getOrderID().getUserID());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order", order);
        jsonObject.put("program", program);
        jsonObject.put("discount", discount);
        return jsonObject.toString();
    }

    /**
     * 按顺序获取节目信息
     *
     * @param orders 订单列表
     * @return programs 对应顺序的节目列表
     */
    private List<Program> getPrograms(List<Order> orders) {
        List<Program> programs = new ArrayList<>();
        for (Order order : orders) {
            programs.add(programService.getOneProgram(order.getProgramID()));
        }
        return programs;
    }
}
