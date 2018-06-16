package booker.statistics.dao.impl;

import booker.model.Program;
import booker.model.Venue;
import booker.model.id.ProgramID;
import booker.statistics.dao.PublisherStatisticsDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

        String hql = "select p from Program p where p.caterer=:caterer and YEAR(p.programID.reserve_time)=:year";
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

        String hql = "select p from Program p where p.caterer=:caterer";
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

        int min_col = 0;
        int min_raw = 0;
        int max_col = 0;
        int max_raw = 0;
        if (size.equals("小")) {
            min_col = 5;
            min_raw = 5;
            max_raw = 10;
            max_col = 10;
        } else {
            min_col = 11;
            min_raw = 11;
            max_raw = 16;
            max_col = 16;
        }

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
     * 统计一个节目的各种座位类型的价格区间
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
}
