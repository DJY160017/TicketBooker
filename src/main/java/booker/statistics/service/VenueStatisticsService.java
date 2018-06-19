package booker.statistics.service;

import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface VenueStatisticsService {

    /**
     * 计算平均价格指数（场馆，细分市场，结果）
     *
     * @param venueID 场馆ID
     * @return
     */
    Map<String, Double> averagePriceIndex(int venueID);

    /**
     * 计算收入指数（场馆，细分市场，结果）
     *
     * @param venueID 场馆ID
     * @return
     */
    Map<String, Double> incomeIndex(int venueID);

    /**
     * 计算市场占有率（场馆，细分市场，结果）
     *
     * @param venueID 场馆ID
     * @return
     */
    Map<String, Double> occupyIndex(int venueID);

    /**
     * 场馆预订率（月， 季度，年）
     *
     * @param venueID  场馆D
     * @param unitTime 单位时间
     * @return
     */
    List<TwoDimensionModel> bookIndex(int venueID, UnitTime unitTime);

    /**
     * 平均地域价格
     *
     * @param venueID
     * @return
     */
    double averageAreaPrice(int venueID);

    /**
     * 同等规模平均价格
     *
     * @param venueID
     * @return
     */
    double averageSizePrice(int venueID);

    /**
     * 同等规模,相同地域平均价格
     *
     * @param venueID
     * @return
     */
    double averageSizeAreaPrice(int venueID);

    /**
     * 统计一年的高峰时段
     *
     * @param venueID
     * @param year
     * @return
     */
    List<TwoDimensionModel> countTopTimeRange(int venueID, int year);
}
