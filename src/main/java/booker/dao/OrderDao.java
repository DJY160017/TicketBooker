package booker.dao;

import booker.model.Order;
import booker.model.id.OrderID;
import booker.util.enums.state.OrderState;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao {

    /**
     * 新增订单
     *
     * @param order Order实体
     * @return boolean
     */
    boolean addOrder(Order order);

    /**
     * 根据OrderID获取指定order实体
     *
     * @param orderID 订单ID
     * @return Order
     */
    Order getOrder(OrderID orderID);

    /**
     * 获取自动订票的订单
     *
     * @return List<Order>
     */
    List<Order> getAutoTicketOrders();

    /**
     * 根据状态获取指定用户ID的订单
     *
     * @param userID 用户ID
     * @param orderState 订单状态
     * @return List<Order>
     */
    List<Order> getOrderByState(String userID, OrderState orderState);

    /**
     * 根据状态获取订单
     *
     * @param orderState 订单状态
     * @return List<Order>
     */
    List<Order> getOrderByState(OrderState orderState);

    /**
     * 根据订单时间获取指定用户ID的订单
     *
     * @param userID 用户ID
     * @param start 查询开始时间
     * @param end 查询结束时间
     * @return List<Order>
     */
    List<Order> getOrderByTime(String userID, LocalDateTime start, LocalDateTime end);

    /**
     * 根据订单ID修改指定订单的订单状态
     *
     * @param orderID 订单ID
     * @param orderState 订单状态
     * @return boolean
     */
    boolean updateOrderState(OrderID orderID, OrderState orderState);

    /**
     * 修改订单信息
     *
     * @param order 更新后的ing但实体
     * @return boolean
     */
    boolean updateOrder(Order order);
}
