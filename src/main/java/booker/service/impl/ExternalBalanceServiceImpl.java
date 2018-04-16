package booker.service.impl;

import booker.dao.ExternalBalanceDao;
import booker.model.BankAccount;
import booker.model.ExternalBalance;
import booker.service.ExternalBalanceService;
import booker.util.exception.BalanceInsufficientException;
import booker.util.exception.PasswordInvalidException;
import booker.util.exception.UserNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ExternalBalanceService")
public class ExternalBalanceServiceImpl implements ExternalBalanceService {

    /**
     * dao对象
     */
    private final ExternalBalanceDao externalBalanceDao;

    @Autowired
    public ExternalBalanceServiceImpl(ExternalBalanceDao externalBalanceDao) {
        this.externalBalanceDao = externalBalanceDao;
    }

    /**
     * 转账
     *
     * @param from_account 资金转出的账户
     * @param password     资金转出的账户密码
     * @param to_account   资金转入的账户
     * @param amount       金额
     * @return boolean
     * @throws UserNotExistException        该银行账户不存在
     * @throws PasswordInvalidException     密码不正确
     * @throws BalanceInsufficientException 余额不足
     */
    @Override
    public boolean transferAccounts(String from_account, String password, String to_account, double amount) throws UserNotExistException, PasswordInvalidException, BalanceInsufficientException {
        ExternalBalance from_externalBalance = externalBalanceDao.get(from_account);
        if (from_externalBalance == null) {
            throw new UserNotExistException(from_account);
        }

        if (!password.equals(from_externalBalance.getPassword())) {
            throw new PasswordInvalidException();
        }

        if (Double.doubleToLongBits(amount) > Double.doubleToLongBits(from_externalBalance.getBalance())) {
            throw new BalanceInsufficientException();
        }

        ExternalBalance to_externalBalance = externalBalanceDao.get(to_account);
        if (to_externalBalance == null) {
            throw new UserNotExistException(to_account);
        }
        from_externalBalance.setBalance(from_externalBalance.getBalance() - amount);
        to_externalBalance.setBalance(to_externalBalance.getBalance() + amount);
        externalBalanceDao.updateBalance(from_externalBalance, to_externalBalance);
        return true;
    }

    /**
     * 现场缴费
     *
     * @param to_account 资金转入的账户
     * @param amount     金额
     * @return boolean
     * @throws UserNotExistException 该银行账户不存在
     */
    @Override
    public boolean onSitePay(String to_account, double amount) throws UserNotExistException {
        ExternalBalance to_externalBalance = externalBalanceDao.get(to_account);
        if (to_externalBalance == null) {
            throw new UserNotExistException(to_account);
        }
        to_externalBalance.setBalance(to_externalBalance.getBalance() + amount);
        externalBalanceDao.updateBalance(to_externalBalance);
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
        return externalBalanceDao.getUserAccount(userID);
    }

    /**
     * 添加用户的银行卡号
     *
     * @param bankAccount 用户卡号
     * @return BankAccount
     */
    @Override
    public boolean addBankAccount(BankAccount bankAccount) {
        BankAccount bankAccount1 = externalBalanceDao.getUserAccount(bankAccount.getUserID());
        if (bankAccount1 == null) {
            return externalBalanceDao.addBankAccount(bankAccount);
        } else {
            return externalBalanceDao.updateBankAccount(bankAccount);
        }
    }
}
