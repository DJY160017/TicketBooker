package booker.model;

import booker.model.id.CouponID;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "coupon")
public class Coupon implements Serializable {

    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private CouponID couponID;

    private double price;

    public CouponID getCouponID() {
        return couponID;
    }

    public void setCouponID(CouponID couponID) {
        this.couponID = couponID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
