package booker.service.impl;

import booker.dao.CouponDao;
import booker.model.Coupon;
import booker.model.id.CouponID;
import booker.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CouponService")
public class CouponServiceImpl implements CouponService {

    /**
     * dao对象
     */
    private final CouponDao couponDao;

    @Autowired
    public CouponServiceImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    /**
     * 获取指定用户的所有的优惠卷
     *
     * @param userID 用户ID
     * @return List<Coupon>
     */
    @Override
    public List<Coupon> getCoupons(String userID) {
        return couponDao.getCoupons(userID);
    }

    /**
     * 添加优惠卷
     *
     * @param coupon 优惠卷实体
     * @return boolean
     */
    @Override
    public boolean addCoupon(Coupon coupon) {
        return couponDao.addCoupon(coupon);
    }

    /**
     * 删除优惠卷
     *
     * @param couponID 优惠卷ID
     * @return boolean
     */
    @Override
    public boolean deleteCoupon(CouponID couponID) {
        return couponDao.deleteCoupon(couponID);
    }
}
