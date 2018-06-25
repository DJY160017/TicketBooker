package booker.statistics.dao.impl;

import booker.model.Program;
import booker.model.Venue;
import booker.model.id.ProgramID;
import booker.statistics.dao.PublisherStatisticsDao;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.helper.VenueSizeHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PublisherStatisticsDaoImpl implements PublisherStatisticsDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public PublisherStatisticsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 统计指定ID用户的花销/单位时间（月）
     *
     * @param caterer 用户ID
     * @param year
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<Program> getPrograms(String caterer, int year) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select p from Program p where p.caterer=:caterer and YEAR(p.end_time)=:year order by p.end_time";
        Query query = session.createQuery(hql);
        query.setParameter("year", year);
        query.setParameter("caterer", caterer);
        List<Program> ids = query.list();
        transaction.commit();
        session.close();
        return ids;
    }

    /**
     * 统计指定ID用户的花销/单位时间（季度）
     *
     * @param caterer 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<Program> getPrograms(String caterer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select p from Program p where p.caterer=:caterer order by p.end_time";
        Query query = session.createQuery(hql);
        query.setParameter("caterer", caterer);
        List<Program> ids = query.list();
        transaction.commit();
        session.close();
        return ids;
    }


    /**
     * 根据场馆规模获取场馆
     *
     * @return 场馆列表
     */
    @Override
    public List<Venue> getSmallSizeVenue(String size) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Map<String, Integer> map = VenueSizeHelper.getSizeParm(size);
        int min_col = map.get("min_col");
        int min_raw = map.get("min_raw");
        int max_col = map.get("max_col");
        int max_raw = map.get("max_raw");

        String hql = "select v from Venue v where v.col_num between :min_col and :max_col and v.raw_num between :min_raw and :max_raw";
        Query query = session.createQuery(hql);
        query.setParameter("min_col", min_col);
        query.setParameter("max_col", max_col);
        query.setParameter("min_raw", min_raw);
        query.setParameter("max_raw", max_raw);
        List<Venue> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 获取同一场馆类型,不同节目的座位类型价格区间(1,0,0)
     *
     * @param venueID 场馆ID
     * @return
     */
    @Override
    public Map<String, Double[]> getSmallVenueSeatPriceRange(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select max(t.price), min(t.price), t.seat_type from ticket as t  where t.vid=:venueID GROUP BY t.seat_type";
        Query query = session.createNativeQuery(hql);
        query.setParameter("venueID", venueID);
        List<Object[]> result = query.list();
        Map<String, Double[]> data = new HashMap<>();
        for (Object[] objects : result) {
            String seat = String.valueOf(objects[2]);
            Double[] price = new Double[2];
            price[1] = (Double) objects[0];
            if (objects[1] == null) {
                price[0] = (double) 0;
            } else {
                price[0] = (Double) objects[1];
            }
            data.put(seat, price);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 获取同一场馆,相同节目类型座位类型价格区间(1,0,3)
     *
     * @param venueID     场馆ID
     * @param programType
     * @return
     */
    @Override
    public Map<String, Double[]> getSmallVenueSeatPriceRange(int venueID, String programType) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "SELECT max(t.price), min(t.price), t.seat_type FROM ticket AS t JOIN program AS p ON t.vid = p.vid and t.reserve_time=p.reserve_time " +
                "WHERE t.vid =:venueID and p.type=:programType GROUP BY t.seat_type";
        Query query = session.createNativeQuery(hql);
        query.setParameter("venueID", venueID);
        query.setParameter("programType", programType);
        List<Object[]> result = query.list();
        Map<String, Double[]> data = new HashMap<>();
        for (Object[] objects : result) {
            String seat = String.valueOf(objects[2]);
            Double[] price = new Double[2];
            price[1] = (Double) objects[0];
            if (objects[1] == null) {
                price[0] = (double) 0;
            } else {
                price[0] = (Double) objects[1];
            }
            data.put(seat, price);
        }
        transaction.commit();
        session.close();
        return data;
    }

    /**
     * 统计一个节目的各种座位类型的价格
     *
     * @param programID
     * @return
     */
    @Override
    public Map<String, Double> countProgramRange(ProgramID programID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select distinct t.seatType, t.price from Ticket t where t.ticketID.programID=:programID";
        Query query = session.createQuery(hql);
        query.setParameter("programID", programID);
        List<Object[]> result = query.list();
        Map<String, Double> data = new HashMap<>();
        for (Object[] objects : result) {
            data.put(String.valueOf(objects[0]), (Double) objects[1]);
        }
        transaction.commit();
        session.close();
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String sql = "SELECT\n" +
                "\t(t1.num /(t1.num + t2.num)), t1.reserve_time\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tcount(*) AS num,\n" +
                "\t\t\tt.vid,\n" +
                "\t\t\tt.reserve_time\n" +
                "\t\tFROM\n" +
                "\t\t\t(\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\t*\n" +
                "\t\t\t\tFROM\n" +
                "\t\t\t\t\tprogram\n" +
                "\t\t\t\tWHERE\n" +
                "\t\t\t\t\tcaterer =:userID1\n" +
                "\t\t\t) AS p\n" +
                "\t\tJOIN ticket AS t ON p.vid = t.vid\n" +
                "\t\tAND p.reserve_time = t.reserve_time\n" +
                "\t\tAND t.state = '2'\n" +
                "\t\tGROUP BY\n" +
                "\t\t\tt.vid,\n" +
                "\t\t\tt.reserve_time\n" +
                "\t) AS t1,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tcount(*) AS num,\n" +
                "\t\t\tt.vid,\n" +
                "\t\t\tt.reserve_time\n" +
                "\t\tFROM\n" +
                "\t\t\t(\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\t*\n" +
                "\t\t\t\tFROM\n" +
                "\t\t\t\t\tprogram\n" +
                "\t\t\t\tWHERE\n" +
                "\t\t\t\t\tcaterer =:userID2\n" +
                "\t\t\t) AS p\n" +
                "\t\tJOIN ticket AS t ON p.vid = t.vid\n" +
                "\t\tAND p.reserve_time = t.reserve_time\n" +
                "\t\tAND t.state = '3'\n" +
                "\t\tGROUP BY\n" +
                "\t\t\tt.vid,\n" +
                "\t\t\tt.reserve_time\n" +
                "\t) AS t2\n" +
                "WHERE\n" +
                "\tt1.vid = t2.vid\n" +
                "AND t1.reserve_time = t2.reserve_time order by t1.reserve_time";
        Query query = session.createNativeQuery(sql);
        query.setParameter("userID1", carter);
        query.setParameter("userID2", carter);
        List<Object[]> result = query.list();
        List<TwoDimensionModel> data = new ArrayList<>();
        for (Object[] objects : result) {
            LocalDateTime localDateTime = ((Timestamp) objects[1]).toLocalDateTime();
            Double rate = ((BigDecimal) objects[0]).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            TwoDimensionModel<LocalDateTime, Double> timeDoubleTwoDimensionModel = new TwoDimensionModel<>(localDateTime, rate);
            data.add(timeDoubleTwoDimensionModel);
        }
        transaction.commit();
        session.close();
        return data;
    }
}
