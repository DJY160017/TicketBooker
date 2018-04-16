package booker.dao.impl;

import booker.dao.CouponDao;
import booker.dao.DaoManager;
import booker.model.Coupon;
import booker.model.id.CouponID;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class CouponDaoImplTest {

    private CouponDao couponDao;

    @Before
    public void setUp() throws Exception {
        couponDao = DaoManager.couponDao;
    }

    @Test
    public void getCoupons() throws Exception {
        List<Coupon> coupons = couponDao.getCoupons("15338595517@163.com");
    }

    @Test
    public void addCoupon() throws Exception {
        Coupon coupon = new Coupon();
        CouponID couponID = new CouponID();
        couponID.setUserID("151250035@smail.nju.edu.cn");
        couponID.setTime(LocalDateTime.now().plusDays(15));
        coupon.setCouponID(couponID);
        coupon.setPrice(10);
        couponDao.addCoupon(coupon);

        Coupon coupon1 = new Coupon();
        CouponID couponID1 = new CouponID();
        couponID1.setUserID("151250035@smail.nju.edu.cn");
        couponID1.setTime(LocalDateTime.now().plusDays(18));
        coupon1.setCouponID(couponID1);
        coupon1.setPrice(5);
        couponDao.addCoupon(coupon1);
    }

    @Test
    public void deleteCoupon() throws Exception {
        CouponID couponID = new CouponID();
        couponID.setUserID("15338595517@163.com");
        couponID.setTime(LocalDateTime.of(2018,2,13, 13,49,45));
        couponDao.deleteCoupon(couponID);
    }

}