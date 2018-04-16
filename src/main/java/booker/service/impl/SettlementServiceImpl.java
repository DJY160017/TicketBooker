package booker.service.impl;

import booker.dao.SettlementDao;
import booker.model.Settlement;
import booker.model.id.ProgramID;
import booker.model.id.SettlementID;
import booker.service.ExternalBalanceService;
import booker.service.SettlementService;
import booker.util.enums.state.SettlementState;
import booker.util.exception.BalanceInsufficientException;
import booker.util.exception.PasswordInvalidException;
import booker.util.exception.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service("SettlementService")
public class SettlementServiceImpl implements SettlementService {

    /**
     * dao对象
     */
    private final SettlementDao settlementDao;

    /**
     * dao对象
     */
    private final ExternalBalanceService externalBalanceService;

    /**
     * 系统账户的银行卡号
     */
    private static final String systemAccount = "admin";

    /**
     * 系统账户的密码
     */
    private static final String password = "123456";

    @Autowired
    public SettlementServiceImpl(SettlementDao settlementDao, ExternalBalanceService externalBalanceService) {
        this.settlementDao = settlementDao;
        this.externalBalanceService = externalBalanceService;
    }

    /**
     * 增加结算记录
     *
     * @param settlement 结算记录
     * @return boolean
     */
    @Override
    public boolean add(Settlement settlement) {
        return settlementDao.add(settlement);
    }

    /**
     * 更新结算记录的余额
     *
     * @param programID 节目ID
     * @param price     需要增加的价格
     * @return boolean
     */
    @Override
    public boolean updateStoreBalance(ProgramID programID, double price) {
        Settlement settlement = settlementDao.getSettlementByProgramID(programID);
        settlement.setStoreTotalPrice(settlement.getStoreTotalPrice() + price);
        settlementDao.update(settlement);
        return false;
    }

    /**
     * 对指定ID进行结算
     *
     * @param settlementIDs 结算记录ID集合
     * @return boolean
     */
    @Override
    public boolean settleAccounts(List<SettlementID> settlementIDs) throws UserNotExistException, BalanceInsufficientException, PasswordInvalidException {
        List<Settlement> settlements = settlementDao.getSettlementByID(settlementIDs);
        for (Settlement settlement : settlements) {
            externalBalanceService.transferAccounts(systemAccount, password, settlement.getSettlementID().getVenueAccount(), settlement.getVenueTotalPrice());
            externalBalanceService.transferAccounts(systemAccount, password, settlement.getSettlementID().getStoreAccount(), settlement.getStoreTotalPrice());
        }
        settlementDao.updateUnsettledSettlementState(settlementIDs);
        return true;
    }

    /**
     * 对数据库中所有未结算的记录进行结算
     *
     * @return boolean
     */
    @Override
    public boolean settleAccounts() throws UserNotExistException, BalanceInsufficientException, PasswordInvalidException {
        List<Settlement> settlements = settlementDao.getSettlement(SettlementState.Unsettled);
        for (Settlement settlement : settlements) {
            externalBalanceService.transferAccounts(systemAccount, password, settlement.getSettlementID().getVenueAccount(), settlement.getVenueTotalPrice());
            externalBalanceService.transferAccounts(systemAccount, password, settlement.getSettlementID().getStoreAccount(), settlement.getStoreTotalPrice());
        }
        settlementDao.updateUnsettledSettlementState();
        return true;
    }

    /**
     * 查看未结算记录
     *
     * @return List<Settlement>
     */
    @Override
    public List<Settlement> getUnsettledRecord() {
        return settlementDao.getSettlement(SettlementState.Unsettled);
    }

    /**
     * 根据状态获取结算的记录
     *
     * @param settlementState 记录状态
     * @return List<Settlement>
     */
    @Override
    public List<Settlement> getSettlement(SettlementState settlementState) {
        return settlementDao.getSettlement(settlementState);
    }

    /**
     * 更新记录
     *
     * @param settlementID    记录ID
     * @param settlementState 更新后的信息
     * @return boolean
     */
    @Override
    public boolean updateSettlementState(SettlementID settlementID, SettlementState settlementState) {
        return settlementDao.updateSettlementState(settlementID, settlementState);
    }

    /**
     * 统计场馆指定年份各月收入和总收入
     *
     * @param year 年份
     * @return 各月总收入
     */
    @Override
    public Map<String, Double> countVenueProfit(int year) {
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        List<Settlement> settlements = settlementDao.getSettlement(start, end, SettlementState.AlreadySettled);
        Map<String, Double> result = new TreeMap<>();
        for (int i = 1; i < 13; i++) {
            result.put(String.valueOf(i), 0.0);
        }
        double total_price = 0;
        for (Settlement settlement : settlements) {
            total_price = total_price + settlement.getVenueTotalPrice();
            String month = String.valueOf(settlement.getSettlementID().getSettlement_time().getMonthValue());
            result.put(month, result.get(month) + settlement.getVenueTotalPrice());
        }
        result.put("total_price", total_price);
        return result;
    }
}
