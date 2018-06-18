package booker.dao;

import booker.model.BankAccount;
import booker.model.ExternalBalance;

import java.util.List;

public interface ExternalBalanceDao {

    /**
     * 用于添加账户和余额
     *
     * @param externalBalance 外部账户实体
     * @return boolean
     */
    boolean add(ExternalBalance externalBalance);

    /**
     * 根据userID用于获取外部账户实体
     *
     * @param account 用户银行账户
     * @return ExternalBalance
     */
    ExternalBalance get(String account);

    /**
     * 更新账户余额（余额不足的异常在service层处理）
     *
     * @param externalBalance 已更新余额后的账户实体
     * @return boolean
     */
    boolean updateBalance(ExternalBalance externalBalance);

    /**
     * 更新账户余额（余额不足的异常在service层处理）
     *
     * @param from_externalBalance 已更新余额后的账户实体
     * @param to_externalBalance 已更新余额后的账户实体
     * @return boolean
     */
    boolean updateBalance(ExternalBalance from_externalBalance, ExternalBalance to_externalBalance);

    /**
     * 获取用户的银行卡号
     *
     * @param userID 用户ID
     * @return BankAccount
     */
    BankAccount getUserAccount(String userID);

    /**
     * 添加用户的银行卡号
     *
     * @param bankAccount 用户卡号
     * @return BankAccount
     */
    boolean addBankAccount(BankAccount bankAccount);

    /**
     * 更新用户的银行卡号
     *
     * @param bankAccount 用户卡号
     * @return BankAccount
     */
    boolean updateBankAccount(BankAccount bankAccount);

    /**
     * 获取数据库中所有的外部支付实体
     *
     * @return
     */
    List<ExternalBalance> getAllExternalBalance();
}
