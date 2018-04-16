package booker.dao.impl;

import booker.dao.SettlementDao;
import booker.model.Settlement;
import booker.model.id.ProgramID;
import booker.model.id.SettlementID;
import booker.util.enums.state.SettlementState;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class SettlementDaoImpl implements SettlementDao {

    /**
     * session工厂对象
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public SettlementDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 增加结算记录
     *
     * @param settlement 结算记录
     * @return boolean
     */
    @Override
    public boolean add(Settlement settlement) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(settlement);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 增加结算记录
     *
     * @param settlement 结算记录
     * @return boolean
     */
    @Override
    public boolean update(Settlement settlement) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(settlement);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 过呢根据节目ID获取结算记录
     *
     * @param programID 界面ID
     * @return Settlement
     */
    @Override
    public Settlement getSettlementByProgramID(ProgramID programID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Settlement where programID=:programID";
        Query query = session.createQuery(hql);
        query.setParameter("programID", programID);
        Settlement result = (Settlement) query.list().get(0);
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 获取未结算的记录
     *
     * @param settlementState 记录状态
     * @return List<Settlement>
     */
    @Override
    public List<Settlement> getSettlement(SettlementState settlementState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Settlement where settlementState=:settlementState";
        Query query = session.createQuery(hql);
        query.setParameter("settlementState", settlementState);
        List<Settlement> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据状态获取结算的记录
     *
     * @param start           截止时间
     * @param end             开始时间
     * @param settlementState 记录状态
     * @return List<Settlement>
     */
    @Override
    public List<Settlement> getSettlement(LocalDateTime start, LocalDateTime end, SettlementState settlementState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Settlement s where s.settlementState=:settlementState and s.settlementID.settlement_time >=:startTime and" +
                " s.settlementID.settlement_time<=:endTime";
        Query query = session.createQuery(hql);
        query.setParameter("settlementState", settlementState);
        query.setParameter("startTime", start);
        query.setParameter("endTime", end);
        List<Settlement> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据ID获取需要结算记录
     *
     * @param settlementIDs ID集合
     * @return List<Settlement>
     */
    @Override
    public List<Settlement> getSettlementByID(List<SettlementID> settlementIDs) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Settlement where settlementID in (:settlementIDs)";
        Query query = session.createQuery(hql);
        query.setParameter("settlementIDs", settlementIDs);
        List<Settlement> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 更新记录
     *
     * @param settlementID    记录ID
     * @param settlementState 更新后的信息
     * @return boolean
     */
    @Override
    public boolean updateSettlementState(SettlementID settlementID, SettlementState settlementState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Settlement s set s.settlementState=:settlementState where settlementID=:settlementID";
        Query query = session.createQuery(hql);
        query.setParameter("settlementState", settlementState);
        query.setParameter("settlementID", settlementID);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 更新所有未计算记录
     *
     * @return boolean
     */
    @Override
    public boolean updateUnsettledSettlementState() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Settlement s set s.settlementState=:settlementState where s.settlementState=:preSettlementState";
        Query query = session.createQuery(hql);
        query.setParameter("settlementState", SettlementState.AlreadySettled);
        query.setParameter("preSettlementState", SettlementState.Unsettled);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 更新所有未计算记录
     *
     * @param settlementIDs ID集合
     * @return boolean
     */
    @Override
    public boolean updateUnsettledSettlementState(List<SettlementID> settlementIDs) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Settlement s set s.settlementState=:settlementState where s.settlementID in (:settlementIDs)";
        Query query = session.createQuery(hql);
        query.setParameter("settlementState", SettlementState.AlreadySettled);
        query.setParameter("settlementIDs", settlementIDs);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }
}
