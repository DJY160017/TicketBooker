package booker.dao.impl;

import booker.dao.DaoManager;
import booker.dao.OrderDao;
import booker.dao.TicketDao;
import booker.model.Order;
import booker.model.Ticket;
import booker.model.id.OrderID;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.util.enums.state.OrderState;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDaoImplTest {

    private OrderDao orderDao;

    private TicketDao ticketDao;

    @Before
    public void setUp() throws Exception {
        orderDao = DaoManager.orderDao;
        ticketDao = DaoManager.ticketDao;
    }

    @Test
    public void addOrder() throws Exception {
        Order order = new Order();
        OrderID orderID = new OrderID();
        orderID.setUserID("15338595517@163.com");
        orderID.setOrderTime(LocalDateTime.now());
        order.setOrderID(orderID);
        order.setAutoTicket(false);
        order.setOrderState(OrderState.AlreadyPaid);
        LocalDateTime localDateTime = LocalDateTime.of(2018,2,14 ,13,17,15);
        StringBuilder detail = new StringBuilder();
        List<Ticket> tickets = new ArrayList<>();
        double total_price = 0;
        for (int i = 1; i <= 6; i++) {
            ProgramID programID = new ProgramID();
            programID.setVenueID(201);
            programID.setReserve_time(localDateTime);
            TicketID ticketID = new TicketID();
            ticketID.setProgramID(programID);
            ticketID.setRaw_num(i);
            ticketID.setCol_num(i);
            Ticket ticket = ticketDao.getOneTicket(ticketID);
            total_price = total_price + ticket.getPrice();
            detail.append(String.valueOf(ticketID.getRaw_num())).append("排 ").append(String.valueOf(ticketID.getCol_num())).append("座-").append(String.valueOf(ticket.getPrice())).append("元;");
            ticket.setOrder(order);
            tickets.add(ticket);
        }
        order.setTotal_price(total_price);
        detail.insert(0,"周杰伦演唱会-");
        order.setDetail(detail.toString());
        order.setTickets(tickets);
        orderDao.addOrder(order);
    }

    @Test
    public void getOrder() throws Exception {
        OrderID orderID = new OrderID();
        LocalDateTime localDateTime = LocalDateTime.of(2018,1,29, 15,48,22);
        orderID.setUserID("151250032@smail.nju.edu.cn");
        orderID.setOrderTime(localDateTime);
        Order order = orderDao.getOrder(orderID);
        assertEquals(6, order.getTickets().size());
    }

    @Test
    public void getOrderByState() throws Exception {
        List<Order> orders = orderDao.getOrderByState("151250032@smail.nju.edu.cn", OrderState.AlreadyPaid);
        assertEquals(1, orders.size());
    }

    @Test
    public void getOrderByTime() throws Exception {
        LocalDateTime start = LocalDateTime.of(2018,1,29, 0,0,0);
        LocalDateTime end = LocalDateTime.of(2018,1,30, 0,0,0);
        List<Order> orders = orderDao.getOrderByTime("151250032@smail.nju.edu.cn",start, end);
        assertEquals(1, orders.size());
    }

    @Test
    public void updateOrderState() throws Exception {
        OrderID orderID = new OrderID();
        LocalDateTime localDateTime = LocalDateTime.of(2018,1,29, 15,48,22);
        orderID.setUserID("151250032@smail.nju.edu.cn");
        orderID.setOrderTime(localDateTime);
        orderDao.updateOrderState(orderID, OrderState.Invalid);
    }

    @Test
    public void updateOrder() throws Exception {
        OrderID orderID = new OrderID();
        LocalDateTime localDateTime = LocalDateTime.of(2018,1,29, 15,48,22);
        orderID.setUserID("151250032@smail.nju.edu.cn");
        orderID.setOrderTime(localDateTime);
        Order order = orderDao.getOrder(orderID);
        order.setOrderState(OrderState.AlreadyPaid);
        orderDao.updateOrder(order);
    }

}