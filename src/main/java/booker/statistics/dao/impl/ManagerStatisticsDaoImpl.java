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

        String hql = "select sum(s.venueTotalPrice) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by QURATER(s.settlementID.settlement_time) order by QURATER(s.settlementID.settlement_time)";
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

        String hql = "select sum(s.storeTotalPrice) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by QURATER(s.settlementID.settlement_time) order by QURATER(s.settlementID.settlement_time)";
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
    public List<TwoDimensionModel> averageVenueIncomeByMonth(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.venueTotalPrice),MONTH(s.settlementID.settlement_time) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by MONTH(s.settlementID.settlement_time) order by MONTH(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double total_price = (double) objects[0];
            int month = (int) objects[1];
            int duration = TimeHelper.getMonthLength(year, month);
            double index = total_price / (double) duration;
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>(month, index);
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
    public List<TwoDimensionModel> averageVenueIncomeByQuarter(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.venueTotalPrice),QUARTER(s.settlementID.settlement_time) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by QUARTER(s.settlementID.settlement_time) order by QUARTER(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double total_price = (double) objects[0];
            int quarter = (int) objects[1];
            int duration = TimeHelper.getQuarterLength(year, quarter);
            double index = total_price / (double) duration;
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>(quarter, index);
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
    public List<TwoDimensionModel> averageProgramIncomeByMonth(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.storeTotalPrice),MONTH(s.settlementID.settlement_time) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by MONTH(s.settlementID.settlement_time) order by MONTH(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double total_price = (double) objects[0];
            int month = (int) objects[1];
            int duration = TimeHelper.getMonthLength(year, month);
            double index = total_price / (double) duration;
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>(month, index);
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
    public List<TwoDimensionModel> averageProgramIncomeByQuarter(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(s.storeTotalPrice),QUARTER(s.settlementID.settlement_time) from Settlement s where YEAR(s.settlementID.settlement_time)=:year group by QUARTER(s.settlementID.settlement_time) order by QUARTER(s.settlementID.settlement_time)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            double total_price = (double) objects[0];
            int quarter = (int) objects[1];
            int duration = TimeHelper.getQuarterLength(year, quarter);
            double index = total_price / (double) duration;
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>(quarter, index);
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
    public double turnover(int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(DAY(p.end_time)-DAY(p.start_time)), count(p) from Program p where YEAR(p.start_time)=:year";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        Object[] result = (Object[]) query.list().get(0);
        int total_day = TimeHelper.getYearLength(year) - ((int) result[0]);
        int num = ((int) result[1]) - 1;
        double index = (double) total_day / (double) num;
        transaction.commit();
        session.close();
        return index;
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
        double result = (double) query.list().get(0);
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
}
