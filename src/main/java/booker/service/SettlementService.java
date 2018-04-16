package booker.service;

import booker.model.Settlement;
import booker.model.id.ProgramID;
import booker.model.id.SettlementID;
import booker.util.enums.state.SettlementState;
import booker.util.exception.BalanceInsufficientException;
import booker.util.exception.PasswordInvalidException;
import booker.util.exception.UserNotExistException;

import java.util.List;
import java.util.Map;

public interface SettlementService {

    /**
     * 增加结算记录
     *
     * @param settlement 结算记录
     * @return boolean
     */
    boolean add(Settlement settlement);

    /**
     * 更新结算记录的余额
     *
     * @param programID 节目ID
     * @param price     需要增加的价格
     * @return boolean
     */
    boolean updateStoreBalance(ProgramID programID, double price);

    /**
     * 对指定ID进行结算
     *
     * @param settlementIDs 结算记录ID集合
     * @return boolean
     */
    boolean settleAccounts(List<SettlementID> settlementIDs) throws UserNotExistException, BalanceInsufficientException, PasswordInvalidException;

    /**
     * 对数据库中所有未结算的记录进行结算
     *
     * @return boolean
     */
    boolean settleAccounts() throws UserNotExistException, BalanceInsufficientException, PasswordInvalidException;

    /**
     * 查看未结算记录
     *
     * @return List<Settlement>
     */
    List<Settlement> getUnsettledRecord();

    /**
     * 根据状态获取结算的记录
     *
     * @param settlementState 记录状态
     * @return List<Settlement>
     */
    List<Settlement> getSettlement(SettlementState settlementState);

    /**
     * 更新记录
     *
     * @param settlementID    记录ID
     * @param settlementState 更新后的信息
     * @return boolean
     */
    boolean updateSettlementState(SettlementID settlementID, SettlementState settlementState);

    /**
     * 统计场馆指定年份各月收入
     *
     * @param year 年份
     * @return 各月总收入
     */
    Map<String, Double> countVenueProfit(int year);
}
