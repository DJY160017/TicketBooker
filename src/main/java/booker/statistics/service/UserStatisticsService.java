package booker.statistics.service;

import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;

import java.util.List;
import java.util.Map;

public interface UserStatisticsService {

    /**
     * 统计指定ID用户的花销
     *
     * @param userID   用户ID
     * @param year     指定年份
     * @param unitTime 单位时间（月，季度，年）
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByUnitTime(String userID, UnitTime unitTime, int year);

    /**
     * 统计指定ID用户的花销
     *
     * @param userID   用户ID
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByUnitTime(String userID);

    /**
     * 统计指定用户观看过的节目类型数量（以便统计最爱节目类型）
     *
     * @param userID 用户ID
     * @return 每种节目类型观看的次数
     */
    Map<String, Integer> countPty(String userID);

    /**
     * 统计用户观看节目的地点次数最多地点
     *
     * @param userID 用户ID
     * @return 可能的常住地
     */
    TwoDimensionModel countCity(String userID);

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
