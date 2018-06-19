package booker.statistics.service.impl;

import booker.dao.OrderDao;
import booker.dao.VenueDao;
import booker.model.Program;
import booker.model.Venue;
import booker.statistics.dao.PublisherStatisticsDao;
import booker.statistics.service.PublisherStatisticsService;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;
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
     * 统计指定ID发布者节目的收入
     *
     * @param caterer  发布者ID
     * @param unitTime 单位时间（月，季度，年）
     * @param year     指定年份
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByUnitTime(String caterer, UnitTime unitTime, int year) {
        List<Program> programs = publisherStatisticsDao.getPrograms(caterer, year);
        Map<Integer, Double> map = new HashMap<>();
        for (Program program : programs) {
            double total_price = orderDao.countSumPrice(program.getProgramID());
            Integer tag = createTag(unitTime, program.getStart_time());
            if (!map.keySet().contains(tag)) {
                map.put(tag, 0.0);
            }
            map.put(tag, map.get(tag) + total_price);
        }

        Map<Integer, Double> result = new TreeMap<>(map);
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Integer key : result.keySet()) {
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>(key, result.get(key));
            data.add(twoDimensionModel);
        }
        return data;
    }

    /**
     * 统计指定ID发布者节目的收入
     *
     * @param caterer 发布者ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByUnitTime(String caterer) {
        List<Program> programs = publisherStatisticsDao.getPrograms(caterer);
        Map<Integer, Double> map = new HashMap<>();
        for (Program program : programs) {
            double total_price = orderDao.countSumPrice(program.getProgramID());
            Integer tag = program.getStart_time().getYear();
            if (!map.keySet().contains(tag)) {
                map.put(tag, 0.0);
            }
            map.put(tag, map.get(tag) + total_price);
        }

        Map<Integer, Double> result = new TreeMap<>(map);
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Integer key : result.keySet()) {
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>(key, result.get(key));
            data.add(twoDimensionModel);
        }
        return data;
    }

    /**
     * 获取同一场馆类型不同节目的座位类型价格区间
     *
     * @param venueID 场馆ID
     * @return
     */
    @Override
    public List<Map<String, Double[]>> getSmallVenueSeatPriceRange(int venueID) {
        Venue venue = venueDao.getVenue(venueID);
        List<Program> programs = venue.getPrograms();
        List<Map<String, Double[]>> data = new ArrayList<>();
        for (Program program : programs) {
            Map<String, Double> map = publisherStatisticsDao.countProgramRange(program.getProgramID());
            Map<String, Double[]> result = new HashMap<>();
            for (String key : map.keySet()) {
                String real_key = program.getName() + "/" + key;
                if (!result.keySet().contains(real_key)) {
                    result.put(real_key, new Double[]{0.0, map.get(key)});
                } else {
                    Double[] price = result.get(real_key);
                    double need_price = map.get(key);
                    if (need_price < price[0]) {
                        price[0] = need_price;
                    } else if (need_price > price[1]) {
                        price[1] = need_price;
                    } else if (price[0] == 0.0) { //当且仅当price[0] =0.0
                        price[0] = need_price;
                    }
                    result.put(real_key, price);
                }
            }
            data.add(result);
        }
        return data;
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
            Map<String, Double[]> data = getVenueSeatPriceRange(venue.getPrograms());
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
        Venue venue = venueDao.getVenue(venueID);
        List<Program> programs = venue.getPrograms();
        List<Map<String, Double[]>> data = new ArrayList<>();
        for (Program program : programs) {
            if (program.getProgramType().equals(programType)) {
                Map<String, Double[]> result = new HashMap<>();
                Map<String, Double> map = publisherStatisticsDao.countProgramRange(program.getProgramID());
                for (String key : map.keySet()) {
                    String real_key = program.getName() + "/" + key;
                    if (!result.keySet().contains(real_key)) {
                        result.put(real_key, new Double[]{0.0, map.get(key)});
                    } else {
                        Double[] price = result.get(real_key);
                        double need_price = map.get(key);
                        if (need_price < price[0]) {
                            price[0] = need_price;
                        } else if (need_price > price[1]) {
                            price[1] = need_price;
                        } else if (price[0] == 0.0) { //当且仅当price[0] =0.0
                            price[0] = need_price;
                        }
                        result.put(real_key, price);
                    }
                }
                data.add(result);
            }
        }
        return data;
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
            List<Program> programs = venue.getPrograms();
            for (Program program : programs) {
                if (program.getProgramType().equals(programType)) {
                    Map<String, Double[]> result = new HashMap<>();
                    Map<String, Double> map = publisherStatisticsDao.countProgramRange(program.getProgramID());
                    for (String key : map.keySet()) {
                        String real_key = program.getName() + "/" + key;
                        if (!result.keySet().contains(real_key)) {
                            result.put(real_key, new Double[]{0.0, map.get(key)});
                        } else {
                            Double[] price = result.get(real_key);
                            double need_price = map.get(key);
                            if (need_price < price[0]) {
                                price[0] = need_price;
                            } else if (need_price > price[1]) {
                                price[1] = need_price;
                            } else if (price[0] == 0.0) { //当且仅当price[0] =0.0
                                price[0] = need_price;
                            }
                            result.put(real_key, price);
                        }
                    }
                    data.add(result);
                }
            }
        }
        return data;
    }

    /**
     * @param localDateTime
     * @return
     */
    private Integer createTag(UnitTime unitTime, LocalDateTime localDateTime) {
        if (unitTime.equals(UnitTime.MONTH)) {
            return localDateTime.getMonthValue();
        } else {
            int month = localDateTime.getMonthValue();
            if (month <= 3) {
                return 1;
            } else if (month <= 6) {
                return 2;
            } else if (month <= 9) {
                return 3;
            } else {
                return 4;
            }
        }
    }

    /**
     * 降低一次取场馆的消耗
     *
     * @return
     */
    private Map<String, Double[]> getVenueSeatPriceRange(List<Program> programs) {
        Map<String, Double[]> result = new HashMap<>();
        for (Program program : programs) {
            Map<String, Double> map = publisherStatisticsDao.countProgramRange(program.getProgramID());
            for (String key : map.keySet()) {
                String real_key = program.getName() + "/" + key;
                if (!result.keySet().contains(real_key)) {
                    result.put(real_key, new Double[]{0.0, map.get(key)});
                } else {
                    Double[] price = result.get(real_key);
                    double need_price = map.get(key);
                    if (need_price < price[0]) {
                        price[0] = need_price;
                    } else if (need_price > price[1]) {
                        price[1] = need_price;
                    } else if (price[0] == 0.0) { //当且仅当price[0] =0.0
                        price[0] = need_price;
                    }
                    result.put(real_key, price);
                }
            }

        }
        return result;
    }
}
