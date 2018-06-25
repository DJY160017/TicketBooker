package booker.dao.impl;

import booker.dao.DaoManager;
import booker.dao.ProgramDao;
import booker.dao.TicketDao;
import booker.dao.VenueDao;
import booker.model.Program;
import booker.model.Ticket;
import booker.model.Venue;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.TicketState;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

public class TicketDaoImplTest {

    private TicketDao ticketDao;

    private ProgramDao programDao;

    private VenueDao venueDao;

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
        venueDao = DaoManager.venueDao;
    }

    @Test
    public void addTickets() throws Exception {
        List<Program> programs = programDao.getPlanByState(ProgramState.AlreadyPassed);
        for (Program program : programs) {
            List<Ticket> tickets = new ArrayList<>();
            int raw = program.getVenue().getRaw_num();
            int col = program.getVenue().getCol_num();
            for (int i = 1; i <= raw; i++) {
                for (int j = 1; j < col; j++) {
                    Ticket ticket = new Ticket();
                    TicketID ticketID = new TicketID();
                    ticketID.setProgramID(program.getProgramID());
                    ticketID.setRaw_num(i);
                    ticketID.setCol_num(j);
                    ticket.setTicketID(ticketID);
                    ticket.setSeatType(createAreaName(j, i, col, raw));
                    ticket.setPrice(calculatePrice(ticket.getSeatType()));
                    ticket.setTicketState(TicketState.PendingReservation);
                    ticket.setCheck(false);
                    tickets.add(ticket);
                    System.out.println("col:" + j + " row:" + i + " area:" + ticket.getSeatType());
                }
            }
            ticketDao.addTickets(tickets);
        }
    }

    @Test
    public void addTickets2() throws Exception {
        Venue venue = venueDao.getVenue(342);
        List<Program> programs = venue.getPrograms();
        for (Program program : programs) {
            List<Ticket> tickets = new ArrayList<>();
            int raw = program.getVenue().getRaw_num();
            int col = program.getVenue().getCol_num();
            for (int i = 1; i <= raw; i++) {
                for (int j = 1; j < col; j++) {
                    Ticket ticket = new Ticket();
                    TicketID ticketID = new TicketID();
                    ticketID.setProgramID(program.getProgramID());
                    ticketID.setRaw_num(i);
                    ticketID.setCol_num(j);
                    ticket.setTicketID(ticketID);
                    ticket.setSeatType(createAreaName(j, i, col, raw));
                    ticket.setPrice(calculatePrice(ticket.getSeatType()));
                    ticket.setTicketState(TicketState.PendingReservation);
                    ticket.setCheck(false);
                    tickets.add(ticket);
                    System.out.println("col:" + j + " row:" + i + " area:" + ticket.getSeatType());
                }
            }
            ticketDao.addTickets(tickets);
            System.out.println("-----------------------------------------------------------------");
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

    private double calculatePrice(String area) {
        char type = area.charAt(0);
        double price = 100 + (type - 'A') * 100 + randomPrice(100);
        return price;
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

    private String createAreaName(int col, int row, int col_a, int row_a) {
        Map<Integer, List<Integer>> col_area = createArea(col_a);
        Map<Integer, List<Integer>> row_area = createArea(row_a);

        char result = 'A';
        for (Integer key : col_area.keySet()) {
            if (col_area.get(key).contains(col)) {
                result = (char) (result + (key - 1));
                break;
            }
        }

        for (Integer key : row_area.keySet()) {
            if (row_area.get(key).contains(row)) {
                result = (char) (result + ((key - 1) * col_area.keySet().size()));
                break;
            }
        }
        return String.valueOf(result) + "区";
    }

    private Map<Integer, List<Integer>> createArea(int all) {
        Map<Integer, List<Integer>> area = new HashMap<>();
        if (all >= 15) {
            area.put(1, new ArrayList<>());
            area.put(2, new ArrayList<>());
            area.put(3, new ArrayList<>());

            int temp = all;
            if (all % 3 != 0) {
                temp = temp - 1;
            }
            int per = temp / 3;
            for (int i = 1; i <= temp; i++) {
                if (i <= per) {
                    area.get(1).add(i);
                    continue;
                }

                if (i <= per * 2) {
                    area.get(2).add(i);
                    continue;
                }

                area.get(3).add(i);
            }

            if (all % 3 != 0) {
                int last_2 = area.get(2).get(area.get(2).size() - 1);
                area.get(2).add(last_2 + 1);

                area.get(3).remove(0);
                int last_3 = area.get(3).get(area.get(3).size() - 1);
                area.get(3).add(last_3 + 1);
            }
        } else {
            area.put(1, new ArrayList<>());
            area.put(2, new ArrayList<>());

            int per = all / 2;
            for (int i = 1; i <= all; i++) {
                if (i <= per) {
                    area.get(1).add(i);
                } else {
                    area.get(2).add(i);
                }
            }
        }
        return area;
    }

    private double randomPrice(int max) {
        int price = new Random().nextInt(max);
        if (price < 0) {
            price = 0;
        }
        return price;
    }
}