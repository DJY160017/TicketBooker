package booker.dao;

import booker.model.Settlement;
import booker.model.id.ProgramID;
import booker.model.id.SettlementID;
import booker.util.enums.state.SettlementState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SettlementDao {

    /**
     * 增加结算记录
     *
     * @param settlement 结算记录
     * @return boolean
     */
    boolean add(Settlement settlement);

    /**
     * 增加结算记录
     *
     * @param settlement 结算记录
     * @return boolean
     */
    boolean update(Settlement settlement);

    /**
     * 过呢根据节目ID获取结算记录
     *
     * @param programID 界面ID
     * @return Settlement
     */
    Settlement getSettlementByProgramID(ProgramID programID);

    /**
     * 根据状态获取结算的记录
     *
     * @param settlementState 记录状态
     * @return List<Settlement>
     */
    List<Settlement> getSettlement(SettlementState settlementState);

    /**
     * 根据状态获取结算的记录
     *
     * @param settlementState 记录状态
     * @param end 开始时间
     * @param start 截止时间
     * @return List<Settlement>
     */
    List<Settlement> getSettlement(LocalDateTime start, LocalDateTime end, SettlementState settlementState);

    /**
     * 根据ID获取需要结算记录
     *
     * @param settlementIDs ID集合
     * @return List<Settlement>
     */
    List<Settlement> getSettlementByID(List<SettlementID> settlementIDs);

    /**
     * 更新记录
     *
     * @param settlementID 记录ID
     * @param settlementState 更新后的信息
     * @return boolean
     */
    boolean updateSettlementState(SettlementID settlementID, SettlementState settlementState);

    /**
     * 更新所有未计算记录
     *
     * @return boolean
     */
    boolean updateUnsettledSettlementState();

    /**
     * 更新所有未计算记录
     *
     * @param settlementIDs ID集合
     * @return boolean
     */
    boolean updateUnsettledSettlementState(List<SettlementID> settlementIDs);
}
