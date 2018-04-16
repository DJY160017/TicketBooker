package booker.task;

import booker.util.exception.AccountInvalidException;
import booker.util.exception.EmailInputException;
import booker.util.exception.PositiveIntegerException;
import booker.util.exception.PriceException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateTask {

    private String expression; //存放正则表达式

    /**
     * @param express 传入需要检查不合法符号的邮件
     * @return boolean 是否符合要求规范
     * @author Byron Dong
     */
    public boolean emailDetector(String express) throws EmailInputException {

        expression = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

        if (getResultOfDetector(express)) {
            throw new EmailInputException(); //密码必须是数字与字母的组合
        }
        return true;
    }

    /**
     * @param express 传入需要检查不合法数字
     * @return boolean 是否符合要求规范
     * @author Byron Dong
     */
    public boolean positiveIntegerDetector(String express) throws PositiveIntegerException {

        expression = "^[1-9]\\d*$";

        if (getResultOfDetector(express)) {
            throw new PositiveIntegerException("场馆的座数和列数必须为正整数"); //正整数
        }
        return true;
    }

    /**
     * @param express 传入需要检查不合法数字
     * @return boolean 是否符合要求规范
     * @author Byron Dong
     */
    public boolean markDetector(String express) throws PositiveIntegerException {

        expression = "^[1-9]\\d*$";

        if (getResultOfDetector(express)) {
            throw new PositiveIntegerException("请输入正确的积分格式"); //正整数
        }
        return true;
    }

    /**
     * @param express 传入需要检查不合法数字
     * @return boolean 是否符合要求规范
     * @author Byron Dong
     */
    public boolean priceDetector(String express) throws PriceException {

        expression = "(^[1-9]([0-9]+)?(\\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\\.[0-9]([0-9])?$)";

        if (getResultOfDetector(express)) {
            throw new PriceException(); //金额
        }
        return true;
    }

    /**
     * @param express 传入需要检查不合法数字
     * @return boolean 是否符合要求规范
     * @author Byron Dong
     */
    public boolean accountDetector(String express) throws AccountInvalidException {
        char bit = getBankCardCheckCode(express.substring(0, express.length() - 1));
        if (bit == 'N' || express.charAt(express.length() - 1) != bit) {
            throw new AccountInvalidException();
        }
        return true;
    }

    /**
     * @param express 传入需要检查不合法符号的表达式
     * @return boolean 是否符合要求规范
     * @author Byron Dong
     */
    private boolean getResultOfDetector(String express) {
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(express);
        return !matcher.matches(); //得到匹配正则表达式的结果
    }

    /**
     * 采用luhm算法判断银行卡号的有效性
     *
     * @param nonCheckCodeCardId 银行卡号
     * @return 判断字符
     */
    private char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N'; //如果传的不是数据返回N
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
