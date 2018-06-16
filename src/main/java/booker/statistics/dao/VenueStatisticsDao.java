package booker.statistics.dao;

import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;

import java.time.LocalDateTime;
import java.util.List;

public interface VenueStatisticsDao {

    /**
     * 细分市场的平均预订价格
     *
     * @return
     */
    double venueMarketAveragePrice();

    /**
     * 场馆的收入
     *
     * @param venueID
     * @return
     */
    double venueIncome(int venueID);

    /**
     * 细分市场的收入
     *
     * @return
     */
    double marketIncome();

    /**
     * 场馆的预订次数
     *
     * @param venueID
     * @return
     */
    int venueBookNum(int venueID);

    /**
     * 细分市场的预定次数
     *
     * @return
     */
    int marketBookNum();

    /**
     * 场馆预订率（月）
     *
     * @param venueID 场馆D
     * @return
     */
    List<TwoDimensionModel> bookIndexByMonth(int venueID);

    /**
     * 场馆预订率（季度）
     *
     * @param venueID 场馆D
     * @return
     */
    List<TwoDimensionModel> bookIndexByQuarter(int venueID);

    /**
     * 场馆预订率（年）
     *
     * @param venueID 场馆D
     * @return
     */
    List<TwoDimensionModel> bookIndexByYear(int venueID);

    /**
     * 平均地域价格
     *
     * @param area
     * @return
     */
    double averageAreaPrice(String area);

    /**
     * 同等规模平均价格
     *
     * @param size
     * @return
     */
    double averageSizePrice(String size);

    /**
     * 同等规模,相同地域平均价格
     *
     * @param area
     * @param size
     * @return
     */
    double averageSizeAreaPrice(String area, String size);

    /**
     * 指定场馆的所有预定时间
     *
     * @param venueID 场馆ID
     * @return
     */
    List<LocalDateTime> getAllStartTime(int venueID, int year);
}
