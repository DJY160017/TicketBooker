package booker.dao.impl;

import booker.dao.VenueDao;
import booker.model.Venue;
import booker.util.enums.state.VenueState;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class VenueDaoImpl implements VenueDao {

    /**
     * session工厂对象
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public VenueDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 添加场馆实体
     *
     * @param venue 场馆实体
     * @return boolean
     */
    @Override
    public int addVenue(Venue venue) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(venue);
        transaction.commit();
        session.close();
        return venue.getVenueID();
    }

    /**
     * 获取场馆实体
     *
     * @param venueID 场馆ID
     * @return Venue
     */
    @Override
    public Venue getVenue(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Venue venue = session.get(Venue.class, venueID);
        transaction.commit();
        session.close();
        return venue;
    }

    /**
     * 获取场馆信息
     *
     * @param address 场馆ID
     * @return Venue
     */
    @Override
    public Venue getOneVenue(String address) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Venue where address=:address";
        Query query = session.createQuery(hql);
        query.setParameter("address", address);
        Venue result = (Venue) query.list().get(0);
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 修改场馆信息
     *
     * @param venue 场馆
     * @return boolean
     */
    @Override
    public boolean modifyVenue(Venue venue) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(venue);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据地址查找场馆，判别场馆是否重复
     *
     * @param address 地址
     * @return boolean
     */
    @Override
    public boolean findVenueByAddress(String address) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Venue where address=:address";
        Query query = session.createQuery(hql);
        query.setParameter("address", address);
        List<Venue> result = query.list();
        transaction.commit();
        session.close();
        return !result.isEmpty();
    }

    /**
     * 根据状态获取场馆
     *
     * @param venueState 状态
     * @return List<Venue>
     */
    @Override
    public List<Venue> getVenueByState(VenueState venueState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Venue where venueState=:venueState";
        Query query = session.createQuery(hql);
        query.setParameter("venueState", venueState);
        List<Venue> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据场馆ID，修改指定场馆状态
     *
     * @param venueID    场馆ID
     * @param venueState 状态
     * @return boolean
     */
    @Override
    public boolean updateVenueState(int venueID, VenueState venueState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Venue v set v.venueState=:venueState where venueID=:venueID";
        Query query = session.createQuery(hql);
        query.setParameter("venueState", venueState);
        query.setParameter("venueID", venueID);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据场馆ID，修改指定场馆状态
     *
     * @param alreadyPassedID 已通过场馆ID
     * @return boolean
     */
    @Override
    public boolean updateVenuesState(List<Integer> alreadyPassedID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Venue v set v.venueState=:venueState where venueID in (:venueIDs)";
        Query query_alreadyPassed = session.createQuery(hql);
        query_alreadyPassed.setParameter("venueState", VenueState.AlreadyPassed);
        query_alreadyPassed.setParameter("venueIDs", alreadyPassedID);
        query_alreadyPassed.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 获取指定场馆的最大页数
     *
     * @param area 区域
     * @param key  关键词
     * @return int
     */
    @Override
    public int getMaxRecords(String area, String key) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(v) from Venue v where v.venueState=:venueState and v.address like:area and v.venueID in " +
                "(select v1.venueID from Venue  v1 where v1.address like:areaKey or v1.name like:nameKey)";
        Query query = session.createQuery(hql);
        query.setParameter("venueState", VenueState.AlreadyPassed);
        query.setParameter("area", "%" + area + "%");
        query.setParameter("areaKey", "%" + key + "%");
        query.setParameter("nameKey", "%" + key + "%");
        Long result = (Long) query.list().get(0);
        transaction.commit();
        session.close();
        return result.intValue();
    }

    /**
     * 分页获取节目信息
     *
     * @param area      区域
     * @param key       关键词
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Program>
     */
    @Override
    public List<Venue> getVenues(String area, String key, int pageNow, int page_size) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Venue v where v.venueState=:venueState and v.address like:area and v.venueID in " +
                "(select v1.venueID from Venue  v1 where v1.address like:areaKey or v1.name like:nameKey)";
        Query query = session.createQuery(hql);
        query.setParameter("venueState", VenueState.AlreadyPassed);
        query.setParameter("area", "%" + area + "%");
        query.setParameter("areaKey", "%" + key + "%");
        query.setParameter("nameKey", "%" + key + "%");
        query.setFirstResult((pageNow - 1) * page_size);
        query.setMaxResults(page_size);
        List<Venue> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 统计场馆各个状态的数量
     *
     * @param venueState 场馆状态
     * @return int
     */
    @Override
    public int countVenueState(VenueState venueState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(v.venueID) from Venue v where v.venueState=:venueState";
        Query query = session.createQuery(hql);
        query.setParameter("venueState", venueState);
        Long result = (Long) query.list().get(0);
        transaction.commit();
        session.close();
        return result.intValue();
    }
}
