package booker.task;

import booker.util.helper.FileHelper;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTask {

    /**
     * 系统发件人
     */
    private static final String sender = "ticketbooker0013";

    /**
     * 系统发件人的密码
     */
    private static final String senderPassword = "qazwsxedc1234567";

    /**
     * 邮件session对象
     */
    private Session session;

    public MailTask() {
        FileHelper fileHelper = new FileHelper();
        Properties properties = fileHelper.readMail();
        session = Session.getInstance(properties);
    }

    /**
     * 系统发送邮件
     *
     * @param address 收件人邮箱地址
     * @param idCode  验证码
     * @throws MessagingException
     */
    public void sendMail(String address, String idCode) throws MessagingException {
        try {
            Message msg = new MimeMessage(session);
            String mailContent = "<p>亲爱的" + address + "用户你好！</p><br>" +
                    "<p>感谢您注册TicketBooker，谢谢！</p>" +
                    "<p>这是您的注册验证码" + idCode + ",请尽快去网站注册，防止泄露和失效！</p>";
            msg.setContent(mailContent, "text/html;charset=UTF-8");
            msg.setSubject("TicketBooker网站注册验证邮件");
            msg.setFrom(new InternetAddress(sender + "@163.com"));//设置发件人
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
            Transport transport = session.getTransport();
            transport.connect("smtp.163.com", sender, senderPassword);//设置发件人在该邮箱主机上的用户名密码
            transport.sendMessage(msg, new Address[]{new InternetAddress(address)});
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
