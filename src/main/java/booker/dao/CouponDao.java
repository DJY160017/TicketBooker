package booker.dao;

import booker.model.Coupon;
import booker.model.id.CouponID;

import java.util.List;

public interface CouponDao {

    /**
     * 获取指定用户的所有的优惠卷
     *
     * @param userID 用户ID
     * @return List<Coupon>
     */
    List<Coupon> getCoupons(String userID);

    /**
     * 添加优惠卷
     *
     * @param coupon 优惠卷实体
     * @return boolean
     */
    boolean addCoupon(Coupon coupon);

    /**
     * 删除优惠卷
     *
     * @param couponID 优惠卷ID
     * @return boolean
     */
    boolean deleteCoupon(CouponID couponID);
}
