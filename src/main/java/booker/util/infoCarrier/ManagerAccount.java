package booker.util.infoCarrier;

public class ManagerAccount {

    private final static String account = "admin";

    private final static String system_mail = "************";

    private final static String account_password = "******";

    private ManagerAccount() {}

    public static String  getAccount(){
        return ManagerAccount.account;
    }

    public static String getSystemMail(){
        return ManagerAccount.system_mail;
    }

    public static String getAccountPassword(){
        return ManagerAccount.account_password;
    }
}
