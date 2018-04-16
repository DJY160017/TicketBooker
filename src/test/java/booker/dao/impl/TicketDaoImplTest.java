package booker.dao.impl;

import booker.dao.DaoManager;
import booker.dao.ProgramDao;
import booker.dao.TicketDao;
import booker.model.Program;
import booker.model.Ticket;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.TicketState;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TicketDaoImplTest {

    private TicketDao ticketDao;

    private ProgramDao programDao;

    /**
     * 行方向加价规则
     */
    private Map<Integer[], Double> raw_price_rule;

    /**
     * 列方向加价规则
     */
    private Map<Integer[], Double> col_price_rule;

    @Before
    public void setUp() throws Exception {
        ticketDao = DaoManager.ticketDao;
        programDao = DaoManager.programDao;
    }

    @Test
    public void addTickets() throws Exception {
        List<Program> programs = programDao.getPlanByState(ProgramState.AlreadyPassed);
        for (Program program : programs) {
            List<Ticket> tickets = new ArrayList<>();
            int raw = program.getVenue().getRaw_num();
            int col = program.getVenue().getCol_num();
            for (int i = 1; i <= raw; i++) {
                for (int j = 1; j <= col; j++) {
                    Ticket ticket = new Ticket();
                    TicketID ticketID = new TicketID();
                    ticketID.setProgramID(program.getProgramID());
                    ticketID.setRaw_num(i);
                    ticketID.setCol_num(j);
                    ticket.setTicketID(ticketID);
                    ticket.setPrice(((raw - i) * 20 + calculatePrice(j, col)) + 100);
                    ticket.setTicketState(TicketState.PendingReservation);
                    ticket.setCheck(false);
                    tickets.add(ticket);
                }
            }
            ticketDao.addTickets(tickets);
        }
    }


    @Test
    public void getOneTicket() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 29, 15, 18, 30);
        TicketID ticketID = new TicketID();
        ticketID.setRaw_num(1);
        ticketID.setCol_num(1);
        Ticket ticket = ticketDao.getOneTicket(ticketID);
        assertEquals(180, ticket.getPrice(), 0);
    }

    @Test
    public void updateTicketState() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 29, 15, 18, 30);
        TicketID ticketID = new TicketID();
        ticketID.setRaw_num(1);
        ticketID.setCol_num(1);
        ticketDao.updateTicketState(ticketID, TicketState.AlreadyPaid);
    }

    @Test
    public void updateTicketsState() throws Exception {
        List<TicketID> ticketIDS = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 29, 15, 18, 30);
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                TicketID ticketID = new TicketID();
                ticketID.setRaw_num(i);
                ticketID.setCol_num(j);
                ticketIDS.add(ticketID);
            }
        }
//        ticketIDS.remove(0);
        ticketDao.updateTicketsState(ticketIDS, TicketState.Unpaid);
    }

    @Test
    public void getProgramLowPrice() {
    }

    @Test
    public void getProgramTicket() {
    }

    @Test
    public void getProgramTicket1() {
        ProgramID programID = new ProgramID();
        programID.setVenueID(201);
        LocalDateTime localDateTime = LocalDateTime.of(2018, 2, 14, 13, 17, 15);
        programID.setReserve_time(localDateTime);
        List<Ticket> tickets = ticketDao.getProgramTicket(programID);
        System.out.println("-----------------------");
    }

    private double calculatePrice(int index, int col) {
        int mid = 0;
        if ((col % 2) == 0) {
            mid = col / 2;
        } else {
            mid = (col / 2) + 1;
        }
        if (index <= mid) {
            return (index - 1) * 20;
        } else {
            return (col - index) * 20;
        }
    }


    private double calculatePrice(int raw, int col, double basePrice) {
        double price = basePrice;
        for (Integer[] key : raw_price_rule.keySet()) {
            if (key[0] <= raw && key[1] >= raw) {
                price = price + raw_price_rule.get(key);
            }
        }
        for (Integer[] key : col_price_rule.keySet()) {
            if (key[0] <= col && key[1] >= col) {
                price = price + col_price_rule.get(key);
            }
        }
        return price;
    }
}