package booker.dao.impl;

import booker.dao.CouponDao;
import booker.model.Coupon;
import booker.model.id.CouponID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponDaoImpl implements CouponDao {

    /**
     * session工厂对象
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public CouponDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 获取指定用户的所有的优惠卷
     *
     * @param userID 用户ID
     * @return List<Coupon>
     */
    @Override
    public List<Coupon> getCoupons(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Coupon where couponID.userID=:userID";
        Query query = session.createQuery(hql);
        query.setParameter("userID", userID);
        List<Coupon> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 添加优惠卷
     *
     * @param coupon 优惠卷实体
     * @return boolean
     */
    @Override
    public boolean addCoupon(Coupon coupon) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(coupon);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 删除优惠卷
     *
     * @param couponID 优惠卷ID
     * @return boolean
     */
    @Override
    public boolean deleteCoupon(CouponID couponID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Coupon coupon = session.get(Coupon.class, couponID);
        session.delete(coupon);
        transaction.commit();
        session.close();
        return true;
    }
}
