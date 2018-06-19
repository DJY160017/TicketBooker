package booker.statistics.dao.impl;

import booker.model.id.ProgramID;
import booker.statistics.dao.UserStatisticsDao;
import booker.util.dataModel.TwoDimensionModel;
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
public class UserStatisticsDaoImpl implements UserStatisticsDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserStatisticsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 统计指定ID用户的花销/单位时间（月）（year,unit_time）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByMonth(String userID, int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price),YEAR(o.orderID.orderTime),MONTH(o.orderID.orderTime) from Order o where o.orderID.userID=:userID and YEAR(o.orderID.orderTime)=:year group by YEAR(o.orderID.orderTime), MONTH(o.orderID.orderTime) order by YEAR(o.orderID.orderTime), MONTH(o.orderID.orderTime)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag(String.valueOf(objects[1]) + "/" + String.valueOf(objects[2]));
            twoDimensionModel.setData((Double) objects[0]);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 统计指定ID用户的花销/单位时间（季度）（year,unit_time）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByQuarter(String userID, int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price),YEAR(o.orderID.orderTime), QUARTER(o.orderID.orderTime) from Order o where o.orderID.userID=:userID and YEAR(o.orderID.orderTime)=:year group by YEAR(o.orderID.orderTime),QUARTER(o.orderID.orderTime) order by YEAR(o.orderID.orderTime), QUARTER(o.orderID.orderTime)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag(String.valueOf(objects[1]) + "/" + String.valueOf(objects[2]));
            twoDimensionModel.setData((Double) objects[0]);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 统计指定ID用户的花销/单位时间（年）（year, 全部）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByYearAll(String userID, int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select o.total_price ,o.orderID.orderTime from Order o where o.orderID.userID=:userID and YEAR(o.orderID.orderTime)=:year order by o.orderID.orderTime";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        query.setParameter("year", year);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag(String.valueOf(objects[1]));
            twoDimensionModel.setData((Double) objects[0]);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 统计指定ID用户的花销/单位时间（月） （全部，(unit_time) ）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByAllMonth(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price),MONTH(o.orderID.orderTime) from Order o where o.orderID.userID=:userID group by MONTH(o.orderID.orderTime) order by MONTH(o.orderID.orderTime)";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag(String.valueOf(objects[1])+"月");
            twoDimensionModel.setData((Double) objects[0]);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 统计指定ID用户的花销/单位时间（季度）（全部，(unit_time) ）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByAllQuarter(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price),QUARTER(o.orderID.orderTime) from Order o where o.orderID.userID=:userID group by QUARTER(o.orderID.orderTime) order by QUARTER(o.orderID.orderTime)";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag(String.valueOf(objects[1])+"季度");
            twoDimensionModel.setData((Double) objects[0]);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 统计指定ID用户的花销/单位时间（年）（全部，年 ）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByAllYear(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price),YEAR(o.orderID.orderTime) from Order o where o.orderID.userID=:userID group by YEAR(o.orderID.orderTime) order by YEAR(o.orderID.orderTime)";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<String, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag(String.valueOf(objects[1])+"年");
            twoDimensionModel.setData((Double) objects[0]);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 统计指定用户观看过的节目类型数量（以便统计最爱节目类型）
     *
     * @param userID 用户ID
     * @return 每种节目类型观看的次数
     */
    @Override
    public List<ProgramID> getALlUserProgramID(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select distinct o.programID from Order o where o.orderID.userID=:userID";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<ProgramID> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 获取用户的每笔订单的详细交易价格
     *
     * @param userID 用户ID
     * @return
     */
    @Override
    public List<TwoDimensionModel> getDetailPrice(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select o.orderID.orderTime, o.total_price from Order o where o.orderID.userID=:userID";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<LocalDateTime, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag((LocalDateTime) objects[0]);
            twoDimensionModel.setData((Double) objects[1]);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 获取客户的消费价格区间
     *
     * @param userID 用户ID
     * @return 价格区间
     */
    @Override
    public double[] countCostRange(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select max(o.total_price), min(o.total_price) from Order o where o.orderID.userID=:userID";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        double data[] = new double[2];
        Object[] objects = result.get(0);
        data[0] = (double) objects[0];
        data[1] = (double) objects[1];
        transaction.commit();
        session.close();
        return data;
    }
}
