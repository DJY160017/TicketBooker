package booker.util.exception;

/**
 * Created by Harvey on 2017/3/11.
 */
public class MoreUserLoginException extends Exception {


    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "当前浏览器已有用户登陆，请先退出已登陆账户！";
    }
}
