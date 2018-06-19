package booker.statistics.service;

import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;

import java.util.List;
import java.util.Map;

public interface PublisherStatisticsService {

    /**
     * 统计指定ID用户的花销（year,unit_time）
     *
     * @param caterer   用户ID
     * @param year     指定年份
     * @param unitTime 单位时间（月，季度，年）
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByUnitTime(String caterer, UnitTime unitTime, int year);

    /**
     * 统计指定ID用户的花销（year, 全部）
     *
     * @param caterer 用户ID
     * @param year   指定年份
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByUnitTime(String caterer, int year);

    /**
     * 统计指定ID用户的花销（全部，(unit_time) ）
     *
     * @param caterer 用户ID
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByUnitTime(String caterer, UnitTime unitTime);

    /**
     * 获取用户的每笔订单的详细交易价格(全部， 全部)
     *
     * @param caterer 用户ID
     * @return
     */
    List<TwoDimensionModel> getDetailPrice(String caterer);


    /**
     * 获取同一场馆类型,不同节目的座位类型价格区间(1,0,0)
     *
     * @param venueID 场馆ID
     * @return
     */
    List<Map<String, Double[]>> getSmallVenueSeatPriceRange(int venueID);

    /**
     * 获取同等规模场馆类型,不同节目的座位类型价格区间(0,2,0)
     *
     * @param size 场馆规模
     * @return
     */
    List<Map<String, Double[]>> getSmallSizeSeatPriceRange(String size);

    /**
     * 获取同一场馆,相同节目类型座位类型价格区间(1,0,3)
     *
     * @param venueID 场馆ID
     * @return
     */
    List<Map<String, Double[]>> getSmallVenueSeatPriceRange(int venueID, String programType);

    /**
     * 获取同等规模场馆类型,相同节目类型座位类型价格区间(0,2,3)
     *
     * @param size 场馆规模
     * @return
     */
    List<Map<String, Double[]>> getSmallSizeSeatPriceRange(String size, String programType);
}
