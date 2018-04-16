package booker.model.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class OrderID implements Serializable {

    @Column(name = "mail")
    private String userID;

    @Column(name = "orderTime")
    private LocalDateTime orderTime;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderID orderID = (OrderID) o;

        if (userID != null ? !userID.equals(orderID.userID) : orderID.userID != null) return false;
        return orderTime != null ? orderTime.equals(orderID.orderTime) : orderID.orderTime == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (orderTime != null ? orderTime.hashCode() : 0);
        return result;
    }
}
