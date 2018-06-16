package booker.statistics.service.impl;

import booker.dao.OrderDao;
import booker.dao.VenueDao;
import booker.model.Venue;
import booker.statistics.dao.VenueStatisticsDao;
import booker.statistics.service.VenueStatisticsService;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;
import booker.util.helper.VenueSizeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("VenueStatisticsService")
public class VenueStatisticsServiceImpl implements VenueStatisticsService {

    private final VenueStatisticsDao venueStatisticsDao;

    private final VenueDao venueDao;

    private final OrderDao orderDao;

    @Autowired
    public VenueStatisticsServiceImpl(VenueStatisticsDao venueStatisticsDao, VenueDao venueDao, OrderDao orderDao) {
        this.venueStatisticsDao = venueStatisticsDao;
        this.venueDao = venueDao;
        this.orderDao = orderDao;
    }

    /**
     * 计算平均价格指数（场馆，细分市场，结果）
     *
     * @param venueID 场馆ID
     * @return
     */
    @Override
    public Map<String, Double> averagePriceIndex(int venueID) {
        Venue venue = venueDao.getVenue(venueID);
        double venuePrice = venue.getPrice();
        double marketPrice = venueStatisticsDao.venueMarketAveragePrice();
        double index = venuePrice / marketPrice;
        Map<String, Double> result = new HashMap<>();
        result.put("场馆", venuePrice);
        result.put("细分市场", marketPrice);
        result.put("指数", index);
        return result;
    }

    /**
     * 计算收入指数（场馆，细分市场，结果）
     *
     * @param venueID 场馆ID
     * @return
     */
    @Override
    public Map<String, Double> incomeIndex(int venueID) {
        double venueIncome = venueStatisticsDao.venueIncome(venueID);
        double marketIncome = venueStatisticsDao.marketIncome();
        double index = venueIncome / marketIncome;
        Map<String, Double> result = new HashMap<>();
        result.put("场馆", venueIncome);
        result.put("细分市场", marketIncome);
        result.put("指数", index);
        return result;
    }

    /**
     * 计算市场占有率（场馆，细分市场，结果）
     *
     * @param venueID 场馆ID
     * @return
     */
    @Override
    public Map<String, Double> occupyIndex(int venueID) {
        int venueNum = venueStatisticsDao.venueBookNum(venueID);
        int marketNum = venueStatisticsDao.marketBookNum();
        double index = (double) venueNum / (double) marketNum;
        Map<String, Double> result = new HashMap<>();
        result.put("场馆", (double) venueNum);
        result.put("细分市场", (double) marketNum);
        result.put("指数", index);
        return result;
    }

    /**
     * 场馆预订率（月， 季度，年）
     *
     * @param venueID  场馆D
     * @param unitTime 单位时间
     * @return
     */
    @Override
    public List<TwoDimensionModel> bookIndex(int venueID, UnitTime unitTime) {
        if (unitTime.equals(UnitTime.MONTH)) {
            return venueStatisticsDao.bookIndexByMonth(venueID);
        } else if (unitTime.equals(UnitTime.QUARTER)) {
            return venueStatisticsDao.bookIndexByQuarter(venueID);
        } else {
            return venueStatisticsDao.bookIndexByYear(venueID);
        }
    }

    /**
     * 平均地域价格
     *
     * @param venueID
     * @return
     */
    @Override
    public double averageAreaPrice(int venueID) {
        Venue venue = venueDao.getVenue(venueID);
        return venueStatisticsDao.averageAreaPrice(venue.getCity());
    }

    /**
     * 同等规模平均价格
     *
     * @param venueID
     * @return
     */
    @Override
    public double averageSizePrice(int venueID) {
        Venue venue = venueDao.getVenue(venueID);
        String size = VenueSizeHelper.getSize(venue.getCol_num(), venue.getRaw_num());
        return venueStatisticsDao.averageSizePrice(size);
    }

    /**
     * 同等规模,相同地域平均价格
     *
     * @param venueID
     * @return
     */
    @Override
    public double averageSizeAreaPrice(int venueID) {
        Venue venue = venueDao.getVenue(venueID);
        String size = VenueSizeHelper.getSize(venue.getCol_num(), venue.getRaw_num());
        String city = venue.getCity();
        return venueStatisticsDao.averageSizeAreaPrice(city, size);
    }

    /**
     * 统计一年的高峰时段
     *
     * @param venueID
     * @param year
     * @return
     */
    @Override
    public List<LocalDateTime> countTopTimeRange(int venueID, int year) {
        return venueStatisticsDao.getAllStartTime(venueID, year);
    }
}
