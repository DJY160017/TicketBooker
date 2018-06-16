package booker.statistics.service.impl;

import booker.dao.OrderDao;
import booker.dao.VenueDao;
import booker.model.Order;
import booker.model.Venue;
import booker.statistics.dao.ManagerStatisticsDao;
import booker.statistics.service.ManagerStatisticsService;
import booker.util.dataModel.SuperTwoDimensionModel;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.OrderState;
import booker.util.enums.state.UnitTime;
import booker.util.enums.state.VenueState;
import booker.util.helper.VenueSizeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ManagerStatisticsService")
public class ManagerStatisticsServiceImpl implements ManagerStatisticsService {

    private final ManagerStatisticsDao managerStatisticsDao;

    private final VenueDao venueDao;

    private final OrderDao orderDao;

    @Autowired
    public ManagerStatisticsServiceImpl(ManagerStatisticsDao managerStatisticsDao, VenueDao venueDao, OrderDao orderDao) {
        this.managerStatisticsDao = managerStatisticsDao;
        this.venueDao = venueDao;
        this.orderDao = orderDao;
    }

    /**
     * 场馆的同比增长率（月，季度）
     *
     * @param unitTime 单位时间
     * @param year     年份
     * @return 同比增长率
     */
    @Override
    public List<TwoDimensionModel> venueIncomeIncrease(UnitTime unitTime, int year) {
        List<TwoDimensionModel> result = new ArrayList<>();
        List<Double> now_year = null;
        List<Double> last_year = null;
        if (unitTime.equals(UnitTime.MONTH)) {
            now_year = managerStatisticsDao.venueIncomeIncreaseByMonth(year);
            last_year = managerStatisticsDao.venueIncomeIncreaseByMonth(year - 1);
        } else {
            now_year = managerStatisticsDao.venueIncomeIncreaseByQuarter(year);
            last_year = managerStatisticsDao.venueIncomeIncreaseByQuarter(year - 1);
        }

        for (int i = 0; i < now_year.size(); i++) {
            double index = (now_year.get(i) - last_year.get(i)) / last_year.get(i);
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>(i, index);
            result.add(twoDimensionModel);
        }
        return result;
    }

    /**
     * 节目的同比增长率
     *
     * @param unitTime 单位时间
     * @param year     年份
     * @return 同比增长率
     */
    @Override
    public List<TwoDimensionModel> programIncomeIncrease(UnitTime unitTime, int year) {
        List<TwoDimensionModel> result = new ArrayList<>();
        List<Double> now_year = null;
        List<Double> last_year = null;
        if (unitTime.equals(UnitTime.MONTH)) {
            now_year = managerStatisticsDao.programIncomeIncreaseByMonth(year);
            last_year = managerStatisticsDao.programIncomeIncreaseByMonth(year - 1);
        } else {
            now_year = managerStatisticsDao.programIncomeIncreaseByQuarter(year);
            last_year = managerStatisticsDao.programIncomeIncreaseByQuarter(year - 1);
        }

        for (int i = 0; i < now_year.size(); i++) {
            double index = (now_year.get(i) - last_year.get(i)) / last_year.get(i);
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>(i, index);
            result.add(twoDimensionModel);
        }
        return result;
    }

    /**
     * 场馆的平均收入（月，季度）
     *
     * @param unitTime 单位时间
     * @param year     年份
     * @return 收入
     */
    @Override
    public List<TwoDimensionModel> averageVenueIncome(UnitTime unitTime, int year) {
        if (unitTime.equals(UnitTime.MONTH)) {
            return managerStatisticsDao.averageVenueIncomeByMonth(year);
        } else {
            return managerStatisticsDao.averageVenueIncomeByQuarter(year);
        }
    }

    /**
     * 节目的平均收入（月，季度）
     *
     * @param unitTime 单位时间
     * @param year     年份
     * @return 收入
     */
    @Override
    public List<TwoDimensionModel> averageProgramIncome(UnitTime unitTime, int year) {
        if (unitTime.equals(UnitTime.MONTH)) {
            return managerStatisticsDao.averageProgramIncomeByMonth(year);
        } else {
            return managerStatisticsDao.averageProgramIncomeByQuarter(year);
        }
    }

    /**
     * 场馆的周转(所有的场馆)
     *
     * @param unitTime 单位时间
     * @param year     年份
     * @return 周转
     */
    @Override
    public double turnover(UnitTime unitTime, int year) {
        return managerStatisticsDao.turnover(year);
    }

    /**
     * 场馆经营效益与地域的关系
     *
     * @return 关系
     */
    @Override
    public List<TwoDimensionModel> statisByArea() {
        List<Venue> venues = venueDao.getVenueByState(VenueState.AlreadyPassed);
        Map<String, Double> map = new HashMap<>();
        for (Venue venue : venues) {
            if (!map.keySet().contains(venue.getCity())) {
                double price = managerStatisticsDao.countVenueIncome(venue.getVenueID());
                map.put(venue.getCity(), price);
            } else {
                double price = managerStatisticsDao.countVenueIncome(venue.getVenueID());
                map.put(venue.getCity(), map.get(venue.getCity()) + price);
            }
        }

        List<TwoDimensionModel> result = new ArrayList<>();
        for (String city : map.keySet()) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>(city, map.get(city));
            result.add(twoDimensionModel);
        }

        return result;
    }

    /**
     * 场馆经营效益与规模的关系
     *
     * @return 关系
     */
    @Override
    public List<TwoDimensionModel> statisBySize() {
        List<Venue> venues = venueDao.getVenueByState(VenueState.AlreadyPassed);
        Map<String, Double> map = new HashMap<>();
        for (Venue venue : venues) {
            String size = VenueSizeHelper.getSize(venue.getCol_num(), venue.getRaw_num());
            if (!map.keySet().contains(size)) {
                double price = managerStatisticsDao.countVenueIncome(venue.getVenueID());
                map.put(size, price);
            } else {
                double price = managerStatisticsDao.countVenueIncome(venue.getVenueID());
                map.put(size, map.get(venue.getCity()) + price);
            }
        }

        List<TwoDimensionModel> result = new ArrayList<>();
        for (String size : map.keySet()) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>(size, map.get(size));
            result.add(twoDimensionModel);
        }

        return result;
    }

    /**
     * 场馆经营效益与节目效益的关系
     *
     * @return 关系
     */
    @Override
    public List<SuperTwoDimensionModel> statisByProgramIncome() {
        return managerStatisticsDao.statisByProgramIncome();
    }

    /**
     * 活跃会员与地域的关系
     *
     * @return 关系
     */
    @Override
    public List<TwoDimensionModel> countMemberArea() {
        List<Order> orders = orderDao.getOrderByState(OrderState.AlreadyPaid);
        Map<String, Integer> map = new HashMap<>();
        for (Order order : orders) {
            Venue venue = venueDao.getVenue(order.getProgramID().getVenueID());
            if (!map.keySet().contains(venue.getCity())) {
                map.put(venue.getCity(), 1);
            } else {
                map.put(venue.getCity(), map.get(venue.getCity()) + 1);
            }
        }

        List<TwoDimensionModel> result = new ArrayList<>();
        for (String city : map.keySet()) {
            TwoDimensionModel<String, Integer> twoDimensionModel = new TwoDimensionModel<>(city, map.get(city));
            result.add(twoDimensionModel);
        }
        return result;
    }
}
