package booker.dao.impl;

import booker.dao.*;
import booker.model.BankAccount;
import booker.model.Program;
import booker.model.Settlement;
import booker.model.id.SettlementID;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.SettlementState;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class SettlementDaoImplTest {

    private SettlementDao settlementDao;

    private ExternalBalanceDao externalBalanceDao;

    private VenueDao venueDao;

    private ProgramDao programDao;

    @Before
    public void setUp() throws Exception {
        settlementDao = DaoManager.settlementDao;
        externalBalanceDao = DaoManager.externalBalanceDao;
        venueDao = DaoManager.venueDao;
        programDao = DaoManager.programDao;
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

}