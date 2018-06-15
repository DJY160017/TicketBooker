package booker.statistics.dao;

import booker.model.id.ProgramID;
import booker.util.dataModel.TwoDimensionModel;

import java.util.List;

public interface UserStatisticsDao {

    /**
     * 统计指定ID用户的花销/单位时间（月）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByMonth(String userID, int year);

    /**
     * 统计指定ID用户的花销/单位时间（季度）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByQuarter(String userID, int year);

    /**
     * 统计指定ID用户的花销/单位时间（年）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByYear(String userID);

    /**
     * 统计指定用户观看过的节目类型数量（以便统计最爱节目类型）
     *
     * @param userID      用户ID
     * @param programType 指定节目类型
     * @return 每种节目类型观看的次数
     */
    List<ProgramID> getALlUserProgramID(String userID);

    /**
     * 获取用户的每笔订单的详细交易价格
     *
     * @param userID 用户ID
     * @return
     */
    List<TwoDimensionModel> getDetailPrice(String userID);

    /**
     * 获取客户的消费价格区间
     *
     * @param userID 用户ID
     * @return 价格区间
     */
    int[] countCostRange(String userID);
}
