package booker.statistics.service;

import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;

import java.util.List;
import java.util.Map;

public interface PublisherStatisticsService {

    /**
     * 统计指定ID发布者节目的收入
     *
     * @param caterer  发布者ID
     * @param year     指定年份
     * @param unitTime 单位时间（月，季度，年）
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByUnitTime(String caterer, UnitTime unitTime, int year);

    /**
     * 统计指定ID发布者节目的收入
     *
     * @param caterer 发布者ID
     * @return 以单位时间为单位的统计数据
     */
    List<TwoDimensionModel> costByUnitTime(String caterer);

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
