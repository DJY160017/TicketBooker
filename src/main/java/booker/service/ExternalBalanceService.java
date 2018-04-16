package booker.service;

import booker.model.BankAccount;
import booker.util.exception.BalanceInsufficientException;
import booker.util.exception.PasswordInvalidException;
import booker.util.exception.UserNotExistException;

public interface ExternalBalanceService {

    /**
     * 转账
     *
     * @param from_account 资金转出的账户
     * @param password     资金转出的账户密码
     * @param to_account   资金转入的账户
     * @param amount       金额
     * @return boolean
     * @throws UserNotExistException 该银行账户不存在
     * @throws PasswordInvalidException 密码不正确
     * @throws BalanceInsufficientException 余额不足
     */
    boolean transferAccounts(String from_account, String password, String to_account, double amount) throws UserNotExistException, PasswordInvalidException, BalanceInsufficientException;

    /**
     * 现场缴费
     *
     * @param to_account   资金转入的账户
     * @param amount       金额
     * @return boolean
     * @throws UserNotExistException 该银行账户不存在
     */
    boolean onSitePay(String to_account, double amount) throws UserNotExistException;

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
}
