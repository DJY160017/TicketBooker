package booker.dao.impl;

import booker.dao.DaoManager;
import booker.dao.ExternalBalanceDao;
import booker.model.BankAccount;
import booker.model.ExternalBalance;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ExternalBalanceDaoImplTest {

    private ExternalBalanceDao externalBalanceDao;

    @Before
    public void setUp() throws Exception {
        externalBalanceDao = DaoManager.externalBalanceDao;
    }

    @Test
    public void add() throws Exception {
        String account1 = "6201000000";
        String account2 = "6201020000";
        String account3 = "6201030000";
        String account4 = "6201040000";
        String account5 = "6201050000";
        String account6 = "6203080000";
        for (int i = 0; i < 100; i++) {
            ExternalBalance externalBalance1 = new ExternalBalance();
            externalBalance1.setAccount(account1 + formate(i));
            externalBalance1.setPassword(formate(randomPassword()));
            externalBalance1.setBalance(randomBalance());
            ExternalBalance externalBalance2 = new ExternalBalance();
            externalBalance2.setAccount(account2 + formate(i));
            externalBalance2.setPassword(formate(randomPassword()));
            externalBalance2.setBalance(randomBalance());
            ExternalBalance externalBalance3 = new ExternalBalance();
            externalBalance3.setAccount(account3 + formate(i));
            externalBalance3.setPassword(formate(randomPassword()));
            externalBalance3.setBalance(randomBalance());
            ExternalBalance externalBalance4 = new ExternalBalance();
            externalBalance4.setAccount(account4 + formate(i));
            externalBalance4.setPassword(formate(randomPassword()));
            externalBalance4.setBalance(randomBalance());
            ExternalBalance externalBalance5 = new ExternalBalance();
            externalBalance5.setAccount(account5 + formate(i));
            externalBalance5.setPassword(formate(randomPassword()));
            externalBalance5.setBalance(randomBalance());
            ExternalBalance externalBalance6 = new ExternalBalance();
            externalBalance6.setAccount(account6 + formate(i));
            externalBalance6.setPassword(formate(randomPassword()));
            externalBalance6.setBalance(randomBalance());
            externalBalanceDao.add(externalBalance1);
            externalBalanceDao.add(externalBalance2);
            externalBalanceDao.add(externalBalance3);
            externalBalanceDao.add(externalBalance4);
            externalBalanceDao.add(externalBalance5);
            externalBalanceDao.add(externalBalance6);
        }
    }

    @Test
    public void addBankAccount() throws Exception {
        String account1 = "6201000000";
        String account2 = "6201020000";
        String account3 = "6201030000";
        String account4 = "6201040000";
        //venue
        String account5 = "6201050000";
        String account6 = "6203080000";
        for (int i = 0; i < 100; i++) {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setUserID(String.valueOf(i + 101));
            bankAccount.setAccount(account6 + formate(i));
            externalBalanceDao.addBankAccount(bankAccount);
        }
    }

    @Test
    public void get() throws Exception {
        ExternalBalance externalBalance = externalBalanceDao.get("6201000000000000");
        assertEquals("372449", externalBalance.getPassword());
        assertEquals(451535287.0, externalBalance.getBalance(), 0);
    }

    @Test
    public void updateBalance() throws Exception {
        ExternalBalance externalBalance = externalBalanceDao.get("6201000000000000");
        externalBalance.setBalance(externalBalance.getBalance() - 200);
    }

    private int randomPassword() {
        Random rand = new Random();
        return rand.nextInt(1000000);
    }

    private int randomBalance() {
        Random rand = new Random();
        return rand.nextInt(1000000000);
    }

    private String formate(int num) {
        String str_num = String.valueOf(num);
        for (int i = str_num.length(); i < 6; i++) {
            str_num = "0" + str_num;
        }
        return str_num;
    }

    private String idFormate(int num) {
        String str_num = String.valueOf(num);
        for (int i = str_num.length(); i < 3; i++) {
            str_num = "0" + str_num;
        }
        return str_num;
    }
}