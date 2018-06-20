package booker.statistics.dao.impl;

import booker.model.id.ProgramID;
import booker.statistics.dao.ManagerStatisticsDao;
import booker.util.dataModel.SuperTwoDimensionModel;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.helper.TimeHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ManagerStatisticsDaoImpl implements ManagerStatisticsDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public ManagerStatisticsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 场馆的同比增长率（月）
     *
     * @param year 年份
     * @return 同比增长率
     */
    @Override
    public List<Double> venueIncomeIncreaseByMonth(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.venueTotalPrice) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by MONTH(s.settlementID.settlement_time) order by MONTH(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Double> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 场馆的同比增长率（季度）
     *
     * @param year 年份
     * @return 同比增长率
     */
    @Override
    public List<Double> venueIncomeIncreaseByQuarter(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.venueTotalPrice) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by QUARTER(s.settlementID.settlement_time) order by QUARTER(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Double> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 节目的同比增长率
     *
     * @param year 年份
     * @return 同比增长率
     */
    @Override
    public List<Double> programIncomeIncreaseByMonth(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.storeTotalPrice) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by MONTH(s.settlementID.settlement_time) order by MONTH(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Double> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 节目的同比增长率
     *
     * @param year 年份
     * @return 同比增长率
     */
    @Override
    public List<Double> programIncomeIncreaseByQuarter(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.storeTotalPrice) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by QUARTER(s.settlementID.settlement_time) order by QUARTER(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Double> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 场馆的平均收入（月）
     *
     * @param year 年份
     * @return 收入
     */
    @Override
    public List<SuperTwoDimensionModel> averageVenueIncomeByMonth(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.venueTotalPrice),max(s.venueTotalPrice),MONTH(s.settlementID.settlement_time) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by MONTH(s.settlementID.settlement_time) order by MONTH(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<SuperTwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double total_price = (double) objects[0];
            double max_price = (double) objects[1];
            int month = (int) objects[2];
            int duration = TimeHelper.getMonthLength(year, month);
            double index = total_price / (double) duration;
            SuperTwoDimensionModel<Integer, Double, Double> twoDimensionModel = new SuperTwoDimensionModel<>(month, index, max_price);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 场馆的平均收入（季度）
     *
     * @param year 年份
     * @return 收入
     */
    @Override
    public List<SuperTwoDimensionModel> averageVenueIncomeByQuarter(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.venueTotalPrice),max(s.venueTotalPrice), QUARTER(s.settlementID.settlement_time) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by QUARTER(s.settlementID.settlement_time) order by QUARTER(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<SuperTwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double total_price = (double) objects[0];
            double max_price = (double) objects[1];
            int quarter = (int) objects[2];
            int duration = TimeHelper.getQuarterLength(year, quarter);
            double index = total_price / (double) duration;
            SuperTwoDimensionModel<Integer, Double, Double> twoDimensionModel = new SuperTwoDimensionModel<>(quarter, index, max_price);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 节目的平均收入（月）
     *
     * @param year 年份
     * @return 收入
     */
    @Override
    public List<SuperTwoDimensionModel> averageProgramIncomeByMonth(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.storeTotalPrice),max(s.storeTotalPrice),MONTH(s.settlementID.settlement_time) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by MONTH(s.settlementID.settlement_time) order by MONTH(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<SuperTwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double total_price = (double) objects[0];
            double max_price = (double) objects[1];
            int month = (int) objects[2];
            int duration = TimeHelper.getMonthLength(year, month);
            double index = total_price / (double) duration;
            SuperTwoDimensionModel<Integer, Double, Double> twoDimensionModel = new SuperTwoDimensionModel<>(month, index, max_price);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 节目的平均收入（季度）
     *
     * @param year 年份
     * @return 收入
     */
    @Override
    public List<SuperTwoDimensionModel> averageProgramIncomeByQuarter(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.storeTotalPrice),max(s.storeTotalPrice),QUARTER(s.settlementID.settlement_time) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by QUARTER(s.settlementID.settlement_time) order by QUARTER(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<SuperTwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double total_price = (double) objects[0];
            double max_price = (double) objects[1];
            int quarter = (int) objects[2];
            int duration = TimeHelper.getQuarterLength(year, quarter);
            double index = total_price / (double) duration;
            SuperTwoDimensionModel<Integer, Double, Double> twoDimensionModel = new SuperTwoDimensionModel<>(quarter, index, max_price);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 场馆的周转(所有的场馆)
     *
     * @param year 年份
     * @return 周转天数
     */
    public double turnover(int venueID, int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select p.start_time, p.end_time from Program p where YEAR(p.start_time)=:year and p.programID.venueID=:venueID order by p.start_time, p.end_time";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        query.setParameter("venueID", venueID);
        List<Object[]> result = query.list();
        int turnover_day = 0;
        for (int i = 1; i < result.size(); i++) {
            LocalDateTime end = (LocalDateTime) result.get(i - 1)[1];
            LocalDateTime next_start = (LocalDateTime) result.get(i)[0];
            Duration duration = Duration.between(end, next_start);
            turnover_day = turnover_day + ((Long) duration.toDays()).intValue();
        }
        if (turnover_day == 0 || result.size() <= 1) {
            transaction.commit();
            session.close();
            return 0;
        } else {
            double index = ((double) turnover_day) / ((double) (result.size() - 1));
            transaction.commit();
            session.close();
            return index;
        }
    }

    /**
     * 获取场馆的总收入
     *
     * @param venueID 场馆ID
     * @return 总收入
     */
    @Override
    public double countVenueIncome(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.venueTotalPrice) from Settlement s where s.programID.venueID=:venueID";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        List<Double> resultList = query.list();
        double result = 0;
        if (resultList != null && !resultList.isEmpty()) {
            if (resultList.get(0) != null) {
                result = resultList.get(0);
            }
        }
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 场馆经营效益与节目效益的关系
     *
     * @return 关系
     */
    @Override
    public List<SuperTwoDimensionModel> statisByProgramIncome() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select s.venueTotalPrice, s.storeTotalPrice, s.settlementID.settlement_time from Settlement s";
        Query query = session.createQuery(hql);
        List<Object[]> result = query.list();
        List<SuperTwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double venue_price = (double) objects[0];
            double program_price = (double) objects[1];
            LocalDateTime time = (LocalDateTime) objects[2];
            SuperTwoDimensionModel<LocalDateTime, Double, Double> superTwoDimensionModel = new SuperTwoDimensionModel<>(time, venue_price, program_price);
            data.add(superTwoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 活跃会员与地域的关系
     *
     * @return 关系
     */
    @Override
    public List<TwoDimensionModel> countMemberArea() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(*), v.city from orders as o join venue as v on o.vid=v.vid GROUP BY v.city";
        Query query = session.createNativeQuery(hql);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            String city = String.valueOf(objects[1]);
            int num = ((BigInteger) objects[0]).intValue();
            TwoDimensionModel<String, Integer> twoDimensionModel = new TwoDimensionModel<>(city, num);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }
}
