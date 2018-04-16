package booker.dao.impl;

import booker.dao.MemberDao;
import booker.model.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MemberDaoImpl implements MemberDao {

    /**
     * session工厂对象
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public MemberDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 用于增加会员实体
     *
     * @param member 会员实体
     * @return boolean
     */
    @Override
    public boolean add(Member member) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(member);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据邮箱获取会员实体
     *
     * @param mail 邮箱
     * @return Member
     */
    @Override
    public Member get(String mail) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Member member = session.get(Member.class, mail);
        transaction.commit();
        session.close();
        return member;
    }

    /**
     * 更新会员实体
     *
     * @param member 更新会员信息后的实体
     * @return boolean
     */
    @Override
    public boolean update(Member member) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(member);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 取消当前邮箱的会员资格
     *
     * @param mail 邮箱
     * @return boolean
     */
    @Override
    public boolean cancelMember(String mail) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Member m set m.isCancel=:result where m.mail=:mail";
        Query query = session.createQuery(hql);
        query.setParameter("mail", mail);
        query.setParameter("result", true);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 统计获取系统总注册人数
     *
     * @return 总人数
     */
    @Override
    public int getSignUpUserNum() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(m.mail) from Member m";
        Query query = session.createQuery(hql);
        Long result = (Long) query.list().get(0);
        transaction.commit();
        session.close();
        return result.intValue();
    }

    /**
     * 统计获取每个等级的会员人数
     *
     * @return List<Member>
     */
    @Override
    public List<Member> getLevelMembers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Member";
        Query query = session.createQuery(hql);
        List<Member> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

}
