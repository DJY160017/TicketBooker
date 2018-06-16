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
     * 统计指定ID用户的花销/单位时间（月）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByMonth(String userID, int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price), MONTH(O.orderID.orderTime) from Order o where o.orderID.userID=:userID and YEAR(o.orderID.orderTime)=:year group by QUARTER(o.orderID.orderTime) order by QUARTER(o.orderID.orderTime)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag((Integer) objects[1]);
            twoDimensionModel.setData((Double) objects[0]);
            data.add(twoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 统计指定ID用户的花销/单位时间（季度）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByQuarter(String userID, int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price), QUARTER(O.orderID.orderTime) from Order o where o.orderID.userID=:userID and YEAR(o.orderID.orderTime)=:year group by QUARTER(o.orderID.orderTime) order by QUARTER(o.orderID.orderTime)";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        query.setParameter("userID", userID);
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
     * 统计指定ID用户的花销/单位时间（年）
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByYear(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select sum(o.total_price), YEAR(O.orderID.orderTime) from Order o where o.orderID.userID=:userID group by YEAR(o.orderID.orderTime) order by YEAR(o.orderID.orderTime)";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            TwoDimensionModel<Integer, Double> twoDimensionModel = new TwoDimensionModel<>();
            twoDimensionModel.setTag((Integer) objects[1]);
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
     * @param userID      用户ID
     * @param programType 指定节目类型
     * @return 每种节目类型观看的次数
     */
    @Override
    public List<ProgramID> getALlUserProgramID(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select o.programID from Order o where o.orderID.userID=:userID";
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
    public int[] countCostRange(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select max(o.total_price), min(o.total_price) from Order o where o.orderID.userID=:userID";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<Object[]> result = query.list();
        int data[] = new int[2];
        Object[] objects = result.get(0);
        data[0] = (int) objects[0];
        data[1] = (int) objects[1];
        transaction.commit();
        session.close();
        return data;
    }
}
