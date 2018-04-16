package booker.dao.impl;

import booker.dao.ExternalBalanceDao;
import booker.model.BankAccount;
import booker.model.ExternalBalance;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ExternalBalanceDaoImpl implements ExternalBalanceDao {

    /**
     * session工厂对象
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public ExternalBalanceDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 用于添加账户和余额
     *
     * @param externalBalance 外部账户实体
     * @return boolean
     */
    @Override
    public boolean add(ExternalBalance externalBalance) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(externalBalance);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据userID用于获取外部账户实体
     *
     * @param account 用户银行账户
     * @return ExternalBalance
     */
    @Override
    public ExternalBalance get(String account) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ExternalBalance externalBalance = session.get(ExternalBalance.class, account);
        transaction.commit();
        session.close();
        return externalBalance;
    }

    /**
     * 更新账户余额（余额不足的异常在service层处理）
     *
     * @param externalBalance 已更新余额后的账户实体
     * @return boolean
     */
    @Override
    public boolean updateBalance(ExternalBalance externalBalance) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(externalBalance);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 更新账户余额（余额不足的异常在service层处理）
     *
     * @param from_externalBalance 已更新余额后的账户实体
     * @param to_externalBalance   已更新余额后的账户实体
     * @return boolean
     */
    @Override
    public boolean updateBalance(ExternalBalance from_externalBalance, ExternalBalance to_externalBalance) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(from_externalBalance);
        session.update(to_externalBalance);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 获取用户的银行卡号
     *
     * @param userID 用户ID
     * @return BankAccount
     */
    @Override
    public BankAccount getUserAccount(String userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        BankAccount bankAccount = session.get(BankAccount.class, userID);
        transaction.commit();
        session.close();
        return bankAccount;
    }

    /**
     * 添加用户的银行卡号
     *
     * @param bankAccount 用户卡号
     * @return BankAccount
     */
    @Override
    public boolean addBankAccount(BankAccount bankAccount) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(bankAccount);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 更新用户的银行卡号
     *
     * @param bankAccount 用户卡号
     * @return BankAccount
     */
    @Override
    public boolean updateBankAccount(BankAccount bankAccount) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(bankAccount);
        transaction.commit();
        session.close();
        return true;
    }
}
