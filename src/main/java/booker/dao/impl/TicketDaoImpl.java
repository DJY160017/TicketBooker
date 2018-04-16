package booker.dao.impl;

import booker.dao.TicketDao;
import booker.model.Ticket;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.util.enums.state.TicketState;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TicketDaoImpl implements TicketDao {

    /**
     * session工厂对象
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public TicketDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 批量添加ticket
     *
     * @param tickets ticket list
     * @return boolean
     */
    @Override
    public boolean addTickets(List<Ticket> tickets) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < tickets.size(); i++) {
            session.save(tickets.get(i));
            if (i % 50 == 0) {
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据ticketID获取一张票
     *
     * @param ticketID 票ID
     * @return Ticket
     */
    @Override
    public Ticket getOneTicket(TicketID ticketID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Ticket ticket = session.get(Ticket.class, ticketID);
        transaction.commit();
        session.close();
        return ticket;
    }

    /**
     * 更新票的信息
     *
     * @param ticket 票
     * @return Ticket
     */
    @Override
    public boolean updateOneTicket(Ticket ticket) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(ticket);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据节目的最低价票
     *
     * @param programID 节目ID
     * @return double
     */
    @Override
    public double getProgramLowPrice(ProgramID programID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select min(t.price) from Ticket t where t.ticketID.programID=:programID";
        Query query = session.createQuery(hql);
        query.setParameter("programID", programID);
        double result = (double) query.list().get(0);
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据节目的时间，地址，状态获取ticket list
     *
     * @param programID   节目ID
     * @param ticketState 票的状态
     * @return List<Ticket>
     */
    @Override
    public List<Ticket> getProgramTicket(ProgramID programID, TicketState ticketState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Ticket where ticketID.programID=:programID and ticketState=:ticketState";
        Query query = session.createQuery(hql);
        query.setParameter("programID", programID);
        query.setParameter("ticketState", ticketState);
        List<Ticket> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }


    /**
     * 根据节目ID，状态获取ticket list
     *
     * @param programID 节目ID
     * @return List<Ticket>
     */
    @Override
    public List<Ticket> getProgramTicket(ProgramID programID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Ticket where ticketID.programID=:programID";
        Query query = session.createQuery(hql);
        query.setParameter("programID", programID);
        List<Ticket> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据ticketID修改状态
     *
     * @param ticketID    票ID
     * @param ticketState 状态
     * @return boolean
     */
    @Override
    public boolean updateTicketState(TicketID ticketID, TicketState ticketState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Ticket t set t.ticketState=:ticketState where ticketID=:ticketID";
        Query query = session.createQuery(hql);
        query.setParameter("ticketState", ticketState);
        query.setParameter("ticketID", ticketID);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据ticketID批量修改状态
     *
     * @param ticketIDs   id列表
     * @param ticketState 状态
     * @return boolean
     */
    @Override
    public boolean updateTicketsState(List<TicketID> ticketIDs, TicketState ticketState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Ticket t set t.ticketState=:ticketState where t.ticketID in (:ticketIDs)";
        Query query = session.createQuery(hql);
        query.setParameter("ticketState", ticketState);
        query.setParameter("ticketIDs", ticketIDs);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 获取所有的票
     *
     * @return List<Ticket>
     */
    @Override
    public List<Ticket> getAllTicket() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Ticket t where t.ticketState=:state";
        Query query = session.createQuery(hql);
        query.setParameter("state", TicketState.Invalid);
        List<Ticket> tickets = query.list();
        transaction.commit();
        session.close();
        return tickets;
    }
}
