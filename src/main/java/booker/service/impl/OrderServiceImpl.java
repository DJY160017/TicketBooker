package booker.service.impl;

import booker.dao.OrderDao;
import booker.model.Order;
import booker.model.Ticket;
import booker.model.id.OrderID;
import booker.model.id.TicketID;
import booker.service.ExternalBalanceService;
import booker.service.OrderService;
import booker.service.ProgramService;
import booker.service.TicketService;
import booker.util.enums.state.OrderState;
import booker.util.enums.state.TicketState;
import booker.util.exception.BalanceInsufficientException;
import booker.util.exception.PasswordInvalidException;
import booker.util.exception.ProgramInvalidException;
import booker.util.exception.UserNotExistException;
import booker.util.infoCarrier.ManagerAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final ExternalBalanceService externalBalanceService;

    private final ProgramService programService;

    private final TicketService ticketService;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, ExternalBalanceService externalBalanceService, ProgramService programService, TicketService ticketService) {
        this.orderDao = orderDao;
        this.externalBalanceService = externalBalanceService;
        this.programService = programService;
        this.ticketService = ticketService;
    }

    /**
     * 新增订单
     *
     * @param order Order实体
     * @return boolean
     */
    @Override
    public boolean addOrder(Order order) {
        return orderDao.addOrder(order);
    }

    /**
     * 根据OrderID获取指定order实体
     *
     * @param orderID 订单ID
     * @return Order
     */
    @Override
    public Order getOrder(OrderID orderID) {
        return orderDao.getOrder(orderID);
    }

    /**
     * 获取自动订票的订单
     *
     * @return List<Order>
     */
    @Override
    public List<Order> getAutoTicketOrders() {
        return orderDao.getAutoTicketOrders();
    }

    /**
     * 根据状态获取指定用户ID的订单
     *
     * @param userID     用户ID
     * @param orderState 订单状态
     * @return List<Order>
     */
    @Override
    public List<Order> getOrderByState(String userID, OrderState orderState) {
        return orderDao.getOrderByState(userID, orderState);
    }

    /**
     * 根据状态获取订单
     *
     * @param orderState 订单状态
     * @return List<Order>
     */
    @Override
    public List<Order> getOrderByState(OrderState orderState) {
        return orderDao.getOrderByState(orderState);
    }

    /**
     * 根据订单时间获取指定用户ID的订单
     *
     * @param userID 用户ID
     * @param start  查询开始时间
     * @param end    查询结束时间
     * @return List<Order>
     */
    @Override
    public List<Order> getOrderByTime(String userID, LocalDateTime start, LocalDateTime end) {
        return orderDao.getOrderByTime(userID, start, end);
    }

    /**
     * 根据订单ID修改指定订单的订单状态
     *
     * @param orderID    订单ID
     * @param orderState 订单状态
     * @return boolean
     */
    @Override
    public boolean updateOrderState(OrderID orderID, OrderState orderState) {
        return orderDao.updateOrderState(orderID, orderState);
    }

    /**
     * 修改订单信息
     *
     * @param order 更新后的ing但实体
     * @return boolean
     */
    @Override
    public boolean updateOrder(Order order) {
        return orderDao.updateOrder(order);
    }

    /**
     * 订单退订
     *
     * @param orderID 订单ID
     * @param account 银行账号
     * @return boolean 是否退订成功
     */
    @Override
    public boolean unSubscribe(OrderID orderID, String account) throws UserNotExistException, BalanceInsufficientException, PasswordInvalidException {
        Order order = orderDao.getOrder(orderID);
        //每天都会定时更新订单的状态，已失效订单不会出现退订的操作
        List<Ticket> tickets = order.getTickets();
        if (tickets != null && !tickets.isEmpty()) {
            List<TicketID> ticketIDS = new ArrayList<>();
            for (Ticket ticket : order.getTickets()) {
                ticketIDS.add(ticket.getTicketID());
            }
            ticketService.updateTicketsState(ticketIDS, TicketState.PendingReservation);
        }
        orderDao.updateOrderState(orderID, OrderState.Unsubscribed);
        externalBalanceService.transferAccounts(ManagerAccount.getAccount(), ManagerAccount.getAccountPassword(), account, order.getTotal_price());
        return true;
    }
}
