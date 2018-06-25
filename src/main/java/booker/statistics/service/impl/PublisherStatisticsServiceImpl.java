package booker.statistics.service.impl;

import booker.dao.OrderDao;
import booker.dao.VenueDao;
import booker.model.Program;
import booker.model.Venue;
import booker.statistics.dao.PublisherStatisticsDao;
import booker.statistics.service.PublisherStatisticsService;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;
import booker.util.helper.TimeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service("PublisherStatisticsService'")
public class PublisherStatisticsServiceImpl implements PublisherStatisticsService {

    private final PublisherStatisticsDao publisherStatisticsDao;

    private final OrderDao orderDao;

    private final VenueDao venueDao;

    @Autowired
    public PublisherStatisticsServiceImpl(PublisherStatisticsDao publisherStatisticsDao, OrderDao orderDao, VenueDao venueDao) {
        this.publisherStatisticsDao = publisherStatisticsDao;
        this.orderDao = orderDao;
        this.venueDao = venueDao;
    }

    /**
     * 统计指定ID用户的花销（year,unit_time）
     *
     * @param caterer  用户ID
     * @param unitTime 单位时间（月，季度）
     * @param year     指定年份
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByUnitTime(String caterer, UnitTime unitTime, int year) {
        List<Program> programs = publisherStatisticsDao.getPrograms(caterer, year);
        Map<String, Double> map = new TreeMap<>();
        for (Program program : programs) {
            double total_price = orderDao.countSumPrice(program.getProgramID());
            String tag = String.valueOf(year) + "/" + String.valueOf(TimeHelper.getUnitTimeValue(unitTime, program.getEnd_time()));
            if (!map.keySet().contains(tag)) {
                map.put(tag, 0.0);
            }
            map.put(tag, map.get(tag) + total_price);
        }

        List<TwoDimensionModel> data = new ArrayList<>();
        for (String key : map.keySet()) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>(key, map.get(key));
            data.add(twoDimensionModel);
        }
        return data;
    }

    /**
     * 统计指定ID用户的花销（year, 全部）
     *
     * @param caterer 用户ID
     * @param year    指定年份
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByUnitTime(String caterer, int year) {
        List<Program> programs = publisherStatisticsDao.getPrograms(caterer, year);
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Program program : programs) {
            double total_price = orderDao.countSumPrice(program.getProgramID());
            TwoDimensionModel<LocalDateTime, Double> twoDimensionModel = new TwoDimensionModel<>(program.getEnd_time(), total_price);
            data.add(twoDimensionModel);
        }
        return data;
    }

    /**
     * 统计指定ID用户的花销（全部，(unit_time) ）
     *
     * @param caterer  用户ID
     * @param unitTime
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByUnitTime(String caterer, UnitTime unitTime) {
        List<Program> programs = publisherStatisticsDao.getPrograms(caterer);
        Map<String, Double> map = new TreeMap<>();
        for (Program program : programs) {
            double total_price = orderDao.countSumPrice(program.getProgramID());
            String tag = String.valueOf(TimeHelper.getUnitTimeValue(unitTime, program.getEnd_time()));
            if (!map.keySet().contains(tag)) {
                map.put(tag, 0.0);
            }
            map.put(tag, map.get(tag) + total_price);
        }

        List<TwoDimensionModel> data = new ArrayList<>();
        for (String key : map.keySet()) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>(key, map.get(key));
            data.add(twoDimensionModel);
        }
        return data;
    }

    /**
     * 获取用户的每笔订单的详细交易价格(全部， 全部)
     *
     * @param caterer 用户ID
     * @return
     */
    @Override
    public List<TwoDimensionModel> getDetailPrice(String caterer) {
        List<Program> programs = publisherStatisticsDao.getPrograms(caterer);
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Program program : programs) {
            double total_price = orderDao.countSumPrice(program.getProgramID());
            TwoDimensionModel<LocalDateTime, Double> twoDimensionModel = new TwoDimensionModel<>(program.getEnd_time(), total_price);
            data.add(twoDimensionModel);
        }
        return data;
    }

    /**
     * 获取同一场馆，类型不同节目的座位类型价格区间
     *
     * @param venueID 场馆ID
     * @return
     */
    @Override
    public List<Map<String, Double[]>> getSmallVenueSeatPriceRange(int venueID) {
        List<Map<String, Double[]>> result = new ArrayList<>();
        result.add(publisherStatisticsDao.getSmallVenueSeatPriceRange(venueID));
        return result;
    }

    /**
     * 获取同等规模场馆类型不同节目的座位类型价格区间
     *
     * @param size 场馆规模
     * @return
     */
    @Override
    public List<Map<String, Double[]>> getSmallSizeSeatPriceRange(String size) {
        List<Venue> venues = publisherStatisticsDao.getSmallSizeVenue(size);
        List<Map<String, Double[]>> result = new ArrayList<>();
        for (Venue venue : venues) {
            Map<String, Double[]> data = publisherStatisticsDao.getSmallVenueSeatPriceRange(venue.getVenueID());
            result.add(data);
        }
        return result;
    }

    /**
     * 获取同一场馆,相同节目类型座位类型价格区间(1,0,3)
     *
     * @param venueID     场馆ID
     * @param programType
     * @return
     */
    @Override
    public List<Map<String, Double[]>> getSmallVenueSeatPriceRange(int venueID, String programType) {
        List<Map<String, Double[]>> result = new ArrayList<>();
        result.add(publisherStatisticsDao.getSmallVenueSeatPriceRange(venueID, programType));
        return result;
    }

    /**
     * 获取同等规模场馆类型,相同节目类型座位类型价格区间
     *
     * @param size        场馆规模
     * @param programType
     * @return
     */
    @Override
    public List<Map<String, Double[]>> getSmallSizeSeatPriceRange(String size, String programType) {
        List<Venue> venues = publisherStatisticsDao.getSmallSizeVenue(size);
        List<Map<String, Double[]>> data = new ArrayList<>();
        for (Venue venue : venues) {
            data.add(publisherStatisticsDao.getSmallVenueSeatPriceRange(venue.getVenueID(), programType));
        }
        return data;
    }

    /**
     * 计算节目的上座率
     *
     * @param carter 节目发布者
     * @return 所有节目的上座率
     */
    @Override
    public List<TwoDimensionModel> countSeatRate(String carter) {
        return publisherStatisticsDao.countSeatRate(carter);
    }
}
