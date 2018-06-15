package booker.util.infoCarrier;

public class ManagerAccount {

    private final static String account = "admin";

    private final static String system_mail = "ticketbooker0013@163.com";

    private final static String account_password = "1234567890";

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
