package booker.dao.impl;

import booker.dao.*;
import booker.model.*;
import booker.model.id.OrderID;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.util.enums.state.OrderState;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.TicketState;
import booker.util.helper.TimeHelper;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

public class OrderDaoImplTest {

    private OrderDao orderDao;

    private TicketDao ticketDao;

    private ProgramDao programDao;

    private MemberDao memberDao;

    private VenueDao venueDao;

    @Before
    public void setUp() throws Exception {
        orderDao = DaoManager.orderDao;
        ticketDao = DaoManager.ticketDao;
        programDao = DaoManager.programDao;
        memberDao = DaoManager.memberDao;
        venueDao = DaoManager.venueDao;
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
        LocalDateTime localDateTime = LocalDateTime.of(2018, 2, 14, 13, 17, 15);
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
        detail.insert(0, "周杰伦演唱会-");
        order.setDetail(detail.toString());
        order.setTickets(tickets);
        orderDao.addOrder(order);
    }

    @Test
    public void randomAddOrder() throws Exception {
        List<Member> members = memberDao.getLevelMembers();
        List<Program> programs = programDao.getPlanByState(ProgramState.AlreadyPassed);
        for (Program program : programs) {
            double saleRate = saleRate(program.getProgramType());
            List<Ticket> tickets = ticketDao.getProgramTicket(program.getProgramID());
            int sale_ticket = (int) (tickets.size() * saleRate);
            for (int i = 0; i < sale_ticket; i++) {
                Member member = members.get(createRandomKey(members.size()));
                OrderID orderID = new OrderID();
                orderID.setUserID(member.getMail());
                orderID.setOrderTime(createRandomDate(program.getProgramID().getReserve_time()));
                Order test_order = orderDao.getOrder(orderID);
                while (test_order != null) {
                    orderID.setOrderTime(createRandomDate(program.getProgramID().getReserve_time()));
                    test_order = orderDao.getOrder(orderID);
                }
                Order order = new Order();
                order.setOrderID(orderID);
                order.setProgramID(program.getProgramID());
                order.setAutoTicket(true);
                order.setOrderState(OrderState.AlreadyPaid);
                order.setTotal_price(tickets.get(i).getPrice());
                order.setDetail("无");
                Ticket ticket = tickets.get(i);
                ticket.setTicketState(TicketState.AlreadyPaid);
                ticket.setOrder(order);
                List<Ticket> new_tickets = new ArrayList<>();
                new_tickets.add(ticket);
                order.setTickets(new_tickets);
                orderDao.addOrder(order);
                System.out.println("add order: " + order.getOrderID().getUserID() + "---" + order.getOrderID().getOrderTime().toString());
            }
            for (int i = sale_ticket; i < tickets.size(); i++) {
                Ticket ticket = tickets.get(i);
                ticket.setTicketState(TicketState.Invalid);
                ticketDao.updateOneTicket(ticket);
                System.out.println("update unuse ticket: " + ticket.getTicketID().getProgramID().getVenueID() + "--" + ticket.getTicketID().getCol_num() + "--" + ticket.getTicketID().getRaw_num());
            }
        }
    }

    @Test
    public void randomAddOrder2() throws Exception {
        Venue venue = venueDao.getVenue(342);
        List<Member> members = memberDao.getLevelMembers();
        List<Program> programs = venue.getPrograms();
        for (Program program : programs) {
            double saleRate = saleRate(program.getProgramType());
            List<Ticket> tickets = ticketDao.getProgramTicket(program.getProgramID());
            int sale_ticket = (int) (tickets.size() * saleRate);
            for (int i = 0; i < sale_ticket; i++) {
                Member member = members.get(createRandomKey(members.size()));
                OrderID orderID = new OrderID();
                orderID.setUserID(member.getMail());
                orderID.setOrderTime(createRandomDate(program.getProgramID().getReserve_time()));
                Order test_order = orderDao.getOrder(orderID);
                while (test_order != null) {
                    orderID.setOrderTime(createRandomDate(program.getProgramID().getReserve_time()));
                    test_order = orderDao.getOrder(orderID);
                }
                Order order = new Order();
                order.setOrderID(orderID);
                order.setProgramID(program.getProgramID());
                order.setAutoTicket(true);
                order.setOrderState(OrderState.AlreadyPaid);
                order.setTotal_price(tickets.get(i).getPrice());
                order.setDetail("无");
                Ticket ticket = tickets.get(i);
                ticket.setTicketState(TicketState.AlreadyPaid);
                ticket.setOrder(order);
                List<Ticket> new_tickets = new ArrayList<>();
                new_tickets.add(ticket);
                order.setTickets(new_tickets);
                orderDao.addOrder(order);
                System.out.println("add order: " + order.getOrderID().getUserID() + "---" + order.getOrderID().getOrderTime().toString());
            }
            for (int i = sale_ticket; i < tickets.size(); i++) {
                Ticket ticket = tickets.get(i);
                ticket.setTicketState(TicketState.Invalid);
                ticketDao.updateOneTicket(ticket);
                System.out.println("update unuse ticket: " + ticket.getTicketID().getProgramID().getVenueID() + "--" + ticket.getTicketID().getCol_num() + "--" + ticket.getTicketID().getRaw_num());
            }
        }
    }

    @Test
    public void getOrder() throws Exception {
        OrderID orderID = new OrderID();
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 29, 15, 48, 22);
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
        LocalDateTime start = LocalDateTime.of(2018, 1, 29, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 1, 30, 0, 0, 0);
        List<Order> orders = orderDao.getOrderByTime("151250032@smail.nju.edu.cn", start, end);
        assertEquals(1, orders.size());
    }

    @Test
    public void updateOrderState() throws Exception {
        OrderID orderID = new OrderID();
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 29, 15, 48, 22);
        orderID.setUserID("151250032@smail.nju.edu.cn");
        orderID.setOrderTime(localDateTime);
        orderDao.updateOrderState(orderID, OrderState.Invalid);
    }

    @Test
    public void updateOrder() throws Exception {
        OrderID orderID = new OrderID();
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 29, 15, 48, 22);
        orderID.setUserID("151250032@smail.nju.edu.cn");
        orderID.setOrderTime(localDateTime);
        Order order = orderDao.getOrder(orderID);
        order.setOrderState(OrderState.AlreadyPaid);
        orderDao.updateOrder(order);
    }

    private double saleRate(String type) {
        double max = 1;
        double min = 0;
        if (type.equals("演唱会")) {
            min = 0.6;
        } else if (type.equals("音乐会")) {
            min = 0.6;
        } else if (type.equals("话剧歌剧")) {
            min = 0.5;
        } else if (type.equals("体育赛事")) {
            min = 0.5;
        } else if (type.equals("舞蹈芭蕾")) {
            min = 0.3;
        } else if (type.equals("儿童亲子")) {
            min = 0.1;
        } else if (type.equals("曲艺杂谈")) {
            min = 0.05;
        } else if (type.equals("展览休闲")) {
            min = 0.05;
        }

        double random = Math.random();
        if (random > 0.999) {
            random = 1;
        }
        double r = random * (max - min) + min;
        BigDecimal bg = new BigDecimal(r);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private Integer createRandomKey(Integer maxVal) {
        Integer v = new Random().nextInt(maxVal);
        if (v <= (Integer) 0) {
            v = 0;
        }
        return v;
    }

    private LocalDateTime createRandomDate(LocalDateTime localDateTime) {
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfMonth();
        int year = localDateTime.getYear();
        Random random = new Random();
        Integer new_month = random.nextInt(13);
        if (new_month <= (Integer) 0) {
            new_month = 1;
        }

        if (new_month > month) {
            year = year - 1;
            day = random.nextInt(TimeHelper.getMonthLength(year, new_month));
            if (day <= 0) {
                day = 1;
            }
        } else if (new_month == month) {
            int new_day = random.nextInt(TimeHelper.getMonthLength(year, new_month));
            if (new_day <= 0) {
                new_day = 1;
            }
            if (new_day >= day) {
                new_month = new_month - 1;
                if (new_month == 0) {
                    new_month = 12;
                    year = year - 1;
                }
            }
        } else {
            day = random.nextInt(TimeHelper.getMonthLength(year, new_month));
            if (day <= 0) {
                day = 1;
            }
        }

        if (new_month == 2 && day >= 30) {
            day = 28;
        }

        LocalDate localDate = LocalDate.of(year, new_month, day);
        int hour = random.nextInt(24);
        if (hour < 0) {
            hour = 0;
        }
        int minute = random.nextInt(60);
        if (minute < 0) {
            minute = 0;
        }
        int second = random.nextInt(60);
        if (second < 0) {
            second = 0;
        }
        LocalTime localTime = LocalTime.of(hour, minute, second);
        return LocalDateTime.of(localDate, localTime);
    }
}