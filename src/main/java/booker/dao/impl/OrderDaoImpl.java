package booker.dao.impl;

import booker.dao.OrderDao;
import booker.model.Order;
import booker.model.id.OrderID;
import booker.util.enums.state.OrderState;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    /**
     * session工厂对象
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public OrderDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 新增订单
     *
     * @param order Order实体
     * @return boolean
     */
    @Override
    public boolean addOrder(Order order) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(order);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据OrderID获取指定order实体
     *
     * @param orderID 订单ID
     * @return Order
     */
    @Override
    public Order getOrder(OrderID orderID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Order order = session.get(Order.class, orderID);
        transaction.commit();
        session.close();
        return order;
    }

    /**
     * 获取自动订票的订单
     *
     * @return List<Order>
     */
    @Override
    public List<Order> getAutoTicketOrders() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Order where isAutoTicket=:autoTicket and orderState=:orderState";
        Query query = session.createQuery(hql);
        query.setParameter("autoTicket", true);
        query.setParameter("orderState", OrderState.AlreadyPaid);
        List<Order> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据状态获取指定用户ID的订单
     *
     * @param userID     用户ID
     * @param orderState 订单状态
     * @return List<Order>
     */
    @Override
    public List<Order> getOrderByState(String userID, OrderState orderState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Order where orderID.userID=:userID and orderState=:orderState";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        query.setParameter("orderState", orderState);
        List<Order> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据状态获取订单
     *
     * @param orderState 订单状态
     * @return List<Order>
     */
    @Override
    public List<Order> getOrderByState(OrderState orderState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Order where orderState=:orderState";
        Query query = session.createQuery(hql);
        query.setParameter("orderState", orderState);
        List<Order> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据订单时间获取指定用户ID的订单
     *
     * @param userID 用户ID
     * @param start  查询开始时间
     * @param end    查询结束时间
     * @return List<Order>
     */
    @Override
    public List<Order> getOrderByTime(String userID, LocalDateTime start, LocalDateTime end) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Order o where o.orderID.userID=:userID and  o.orderID.orderTime<=:endTime " +
                "and o.orderID.orderTime>=:startTime";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        query.setParameter("endTime", end);
        query.setParameter("startTime", start);
        List<Order> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据订单ID修改指定订单的订单状态
     *
     * @param orderID    订单ID
     * @param orderState 订单状态
     * @return boolean
     */
    @Override
    public boolean updateOrderState(OrderID orderID, OrderState orderState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Order o set o.orderState=:orderState where orderID=:orderID";
        Query query = session.createQuery(hql);
        query.setParameter("orderState", orderState);
        query.setParameter("orderID", orderID);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 修改订单信息
     *
     * @param order 更新后的订单实体
     * @return boolean
     */
    @Override
    public boolean updateOrder(Order order) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(order);
        transaction.commit();
        session.close();
        return true;
    }
}
