package booker.dao.impl;

import booker.dao.*;
import booker.model.BankAccount;
import booker.model.Program;
import booker.model.Settlement;
import booker.model.Venue;
import booker.model.id.SettlementID;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.SettlementState;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SettlementDaoImplTest {

    private SettlementDao settlementDao;

    private ExternalBalanceDao externalBalanceDao;

    private VenueDao venueDao;

    private ProgramDao programDao;

    private OrderDao orderDao;

    @Before
    public void setUp() throws Exception {
        settlementDao = DaoManager.settlementDao;
        externalBalanceDao = DaoManager.externalBalanceDao;
        venueDao = DaoManager.venueDao;
        programDao = DaoManager.programDao;
        orderDao = DaoManager.orderDao;
    }

    @Test
    public void add() throws Exception {
        List<Program> programs = programDao.getPlanByState(ProgramState.AlreadyPassed);
        for (int i = 0; i < programs.size(); i++) {
            Program program = programs.get(i);
            Settlement settlement = new Settlement();
            SettlementID settlementID = new SettlementID();
            BankAccount bankAccount = externalBalanceDao.getUserAccount(program.getCaterer());
            settlementID.setStoreAccount(bankAccount.getAccount());
            settlementID.setVenueAccount(externalBalanceDao.getUserAccount(String.valueOf(program.getVenue().getVenueID())).getAccount());
            settlementID.setSettlement_time(LocalDateTime.now().plusMinutes(i));
            settlement.setSettlementID(settlementID);
            settlement.setSettlementState(SettlementState.Unfinished);
            settlement.setProgramID(program.getProgramID());
            settlement.setStoreTotalPrice(0);
            settlement.setVenueTotalPrice(program.getVenue().getPrice());
            settlementDao.add(settlement);
        }
    }

    @Test
    public void getSettlement() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 29, 14, 42, 19);
        List<Settlement> settlements = settlementDao.getSettlement(SettlementState.Unsettled);
        assertEquals(localDateTime, settlements.get(0).getSettlementID().getSettlement_time());
    }

    @Test
    public void updateSettlementState() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 29, 14, 42, 19);
        SettlementID settlementID = new SettlementID();
        settlementID.setStoreAccount("6201000000000000");
        settlementID.setVenueAccount("6201000000000001");
        settlementID.setSettlement_time(localDateTime);
        settlementDao.updateSettlementState(settlementID, SettlementState.AlreadySettled);
    }

    @Test
    public void addSettlement() {
        List<Program> programs = programDao.getPlanByState(ProgramState.AlreadyPassed);
        for (Program program : programs) {
            Venue venue = program.getVenue();
            BankAccount catererAccount = externalBalanceDao.getUserAccount(program.getCaterer());
            BankAccount venueAccount = externalBalanceDao.getUserAccount(String.valueOf(venue.getVenueID()));
            double storePrice = orderDao.countSumPrice(program.getProgramID());
            Duration duration = Duration.between(program.getStart_time(), program.getEnd_time());
            double venuePrice = venue.getPrice() * duration.toDays();
            SettlementID settlementID = new SettlementID();
            settlementID.setStoreAccount(catererAccount.getAccount());
            settlementID.setVenueAccount(venueAccount.getAccount());
            settlementID.setSettlement_time(randomeLocalDateTime(program.getEnd_time(), program.getEnd_time().plusDays(5)));
            Settlement test_settlement = settlementDao.getOne(settlementID);
            while (test_settlement != null) {
                settlementID.setSettlement_time(randomeLocalDateTime(program.getEnd_time(), program.getEnd_time().plusDays(5)));
                test_settlement = settlementDao.getOne(settlementID);
            }
            Settlement settlement = new Settlement();
            settlement.setSettlementID(settlementID);
            settlement.setStoreTotalPrice(storePrice);
            settlement.setVenueTotalPrice(venuePrice);
            settlement.setProgramID(program.getProgramID());
            settlement.setSettlementState(SettlementState.AlreadySettled);

            settlementDao.add(settlement);
        }
    }

    @Test
    public void addSettlement2() {
        Venue venue = venueDao.getVenue(342);
        List<Program> programs = venue.getPrograms();
        for (Program program : programs) {
            BankAccount catererAccount = externalBalanceDao.getUserAccount(program.getCaterer());
            BankAccount venueAccount = externalBalanceDao.getUserAccount(String.valueOf(venue.getVenueID()));
            double storePrice = orderDao.countSumPrice(program.getProgramID());
            Duration duration = Duration.between(program.getStart_time(), program.getEnd_time());
            double venuePrice = venue.getPrice() * duration.toDays();
            SettlementID settlementID = new SettlementID();
            settlementID.setStoreAccount(catererAccount.getAccount());
            settlementID.setVenueAccount(venueAccount.getAccount());
            settlementID.setSettlement_time(randomeLocalDateTime(program.getEnd_time(), program.getEnd_time().plusDays(5)));
            Settlement test_settlement = settlementDao.getOne(settlementID);
            while (test_settlement != null) {
                settlementID.setSettlement_time(randomeLocalDateTime(program.getEnd_time(), program.getEnd_time().plusDays(5)));
                test_settlement = settlementDao.getOne(settlementID);
            }
            Settlement settlement = new Settlement();
            settlement.setSettlementID(settlementID);
            settlement.setStoreTotalPrice(storePrice);
            settlement.setVenueTotalPrice(venuePrice);
            settlement.setProgramID(program.getProgramID());
            settlement.setSettlementState(SettlementState.AlreadySettled);
            settlementDao.add(settlement);
        }
    }

    private LocalDateTime randomeLocalDateTime(LocalDateTime start, LocalDateTime end) {
        //LocalDateTime to Date
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt_start = start.atZone(zoneId);
        ZonedDateTime zdt_end = end.atZone(zoneId);
        Date start_date = Date.from(zdt_start.toInstant());
        Date end_date = Date.from(zdt_end.toInstant());

        //根据long进行random日期
        long start_time = start_date.getTime();
        long end_time = end_date.getTime();
        long rtn = start_time + (long) (Math.random() * (end_time - start_time));

        // 把得到的random long  转化为Date -> 转化为LocalDateTime
        Date result = new Date(rtn);
        Instant instant = result.toInstant();
        ZoneId zoneId_result = ZoneId.systemDefault();

        return instant.atZone(zoneId_result).toLocalDateTime();
    }

}