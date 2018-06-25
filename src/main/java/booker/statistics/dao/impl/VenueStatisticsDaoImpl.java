package booker.statistics.dao.impl;

import booker.statistics.dao.VenueStatisticsDao;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.helper.TimeHelper;
import booker.util.helper.VenueSizeHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class VenueStatisticsDaoImpl implements VenueStatisticsDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public VenueStatisticsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 细分市场的平均预订价格
     *
     * @return
     */
    @Override
    public double venueMarketAveragePrice() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(v.price), count(v) from Venue v";
        Query query = session.createQuery(hql);
        Object[] result = (Object[]) query.list().get(0);
        double total_price = (double) result[0];
        int total_num = ((Long) result[1]).intValue();
        double price = total_price / (double) total_num;
        transaction.commit();
        session.close();
        return price;
    }

    /**
     * 场馆的收入
     *
     * @param venueID
     * @return
     */
    @Override
    public double venueIncome(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price) from Order o where o.programID.venueID=:venueID";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        double price = (double) query.list().get(0);
        transaction.commit();
        session.close();
        return price;
    }

    /**
     * 细分市场的收入
     *
     * @return
     */
    @Override
    public double marketIncome() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price) from Order o";
        Query query = session.createQuery(hql);
        double price = (double) query.list().get(0);
        transaction.commit();
        session.close();
        return price;
    }

    /**
     * 场馆的预订次数
     *
     * @param venueID
     * @return
     */
    @Override
    public int venueBookNum(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(p) from Program p where p.programID.venueID=:venueID";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        int num = ((Long) query.list().get(0)).intValue();
        transaction.commit();
        session.close();
        return num;
    }

    /**
     * 细分市场的预定次数
     *
     * @return
     */
    @Override
    public int marketBookNum() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(p) from Program p";
        Query query = session.createQuery(hql);
        int num = ((Long) query.list().get(0)).intValue();
        transaction.commit();
        session.close();
        return num;
    }

    /**
     * 场馆预订率（月）
     *
     * @param venueID 场馆D
     * @return
     */
    @Override
    public List<TwoDimensionModel> bookIndexByMonth(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(DAY(p.end_time)-DAY(p.start_time)), YEAR(p.start_time), MONTH(p.start_time) from Program p where p.programID.venueID=:venueID group by YEAR(p.start_time), MONTH(p.start_time) order by YEAR(p.start_time), MONTH(p.start_time)";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            int days = ((Long) objects[0]).intValue();
            int year = (int) objects[1];
            int month = (int) objects[2];
            int duration = TimeHelper.getMonthLength(year, month);
            double index = (double) days / (double) duration;
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag(String.valueOf(year) + "/" + String.valueOf(month));
            twoDimensionModel.setData(index);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 场馆预订率（季度）
     *
     * @param venueID 场馆D
     * @return
     */
    @Override
    public List<TwoDimensionModel> bookIndexByQuarter(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(DAY(p.end_time)-DAY(p.start_time)), YEAR(p.start_time), QUARTER(p.start_time) from Program p where p.programID.venueID=:venueID group by YEAR(p.start_time), QUARTER(p.start_time) order by YEAR(p.start_time), QUARTER(p.start_time)";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            int days = ((Long) objects[0]).intValue();
            int year = (int) objects[1];
            int quarter = (int) objects[2];
            int duration = TimeHelper.getMonthLength(year, quarter);
            double index = (double) days / (double) duration;
            twoDimensionModel.setTag(String.valueOf(year) + "/" + String.valueOf(quarter));
            twoDimensionModel.setData(index);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 场馆预订率（年）
     *
     * @param venueID 场馆D
     * @return
     */
    @Override
    public List<TwoDimensionModel> bookIndexByYear(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(DAY(p.end_time)-DAY(p.start_time)), YEAR(p.start_time) from Program p where p.programID.venueID=:venueID group by YEAR(p.start_time) order by YEAR(p.start_time)";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            int days = ((Long) objects[0]).intValue();
            int year = (int) objects[1];
            int duration = TimeHelper.getYearLength(year);
            double index = (double) days / (double) duration;
            twoDimensionModel.setTag(String.valueOf(year));
            twoDimensionModel.setData(index);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 平均地域价格
     *
     * @param area
     * @return
     */
    @Override
    public double averageAreaPrice(String area) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(v),sum(v.price) from Venue v where v.city=:area";
        Query query = session.createQuery(hql);
        query.setParameter("area", area);
        Object[] objects = (Object[]) query.list().get(0);
        double total_num = ((Long) objects[0]).doubleValue();
        double total_price = (double) objects[1];
        double index = total_price / total_num;
        transaction.commit();
        session.close();
        return index;
    }

    /**
     * 同等规模平均价格
     *
     * @param size
     * @return
     */
    @Override
    public double averageSizePrice(String size) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Map<String, Integer> map = VenueSizeHelper.getSizeParm(size);
        int min_col = map.get("min_col");
        int min_raw = map.get("min_raw");
        int max_col = map.get("max_col");
        int max_raw = map.get("max_raw");

        String hql = "select count(v),sum(v.price) from Venue v where v.col_num between :min_col and :max_col and v.raw_num between :min_raw and :max_raw";
        Query query = session.createQuery(hql);
        query.setParameter("min_col", min_col);
        query.setParameter("max_col", max_col);
        query.setParameter("min_raw", min_raw);
        query.setParameter("max_raw", max_raw);
        Object[] objects = (Object[]) query.list().get(0);
        double total_num = ((Long) objects[0]).doubleValue();
        double total_price = (double) objects[1];
        double index = total_price / total_num;
        transaction.commit();
        session.close();
        return index;
    }

    /**
     * 同等规模,相同地域平均价格
     *
     * @param area
     * @param size
     * @return
     */
    @Override
    public double averageSizeAreaPrice(String area, String size) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Map<String, Integer> map = VenueSizeHelper.getSizeParm(size);
        int min_col = map.get("min_col");
        int min_raw = map.get("min_raw");
        int max_col = map.get("max_col");
        int max_raw = map.get("max_raw");

        String hql = "select count(v),sum(v.price) from Venue v where v.city=:area and v.col_num between :min_col and :max_col and v.raw_num between :min_raw and :max_raw";
        Query query = session.createQuery(hql);
        query.setParameter("area", area);
        query.setParameter("min_col", min_col);
        query.setParameter("max_col", max_col);
        query.setParameter("min_raw", min_raw);
        query.setParameter("max_raw", max_raw);
        Object[] objects = (Object[]) query.list().get(0);
        double total_num = ((Long) objects[0]).doubleValue();
        double total_price = (double) objects[1];
        double index = total_price / total_num;
        transaction.commit();
        session.close();
        return index;
    }

    /**
     * 指定场馆的所有预定时间
     *
     * @param venueID 场馆ID
     * @return
     */
    @Override
    public List<TwoDimensionModel> getAllStartTime(int venueID, int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select p.start_time,(DAY(p.end_time)-DAY(p.start_time)) from Program p where p.programID.venueID=:venueID and YEAR(p.start_time)=:year order by p.start_time";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        query.setParameter("year", year);
        List<Object[]> times = query.list();
        List<TwoDimensionModel> result = new ArrayList<>();
        for (Object[] objects : times) {
            LocalDateTime start = (LocalDateTime) objects[0];
            int day = (int) objects[1];
            TwoDimensionModel<LocalDateTime, Integer> twoDimensionModel = new TwoDimensionModel<>(start, day);
            result.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return result;
    }
}
