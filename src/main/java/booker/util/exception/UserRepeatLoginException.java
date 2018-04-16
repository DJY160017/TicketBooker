package booker.util.exception;

/**
 * Created by Harvey on 2017/3/11.
 */
public class UserRepeatLoginException extends Exception {

    /**
     * 重复登录的用户ID
     */
    private String userID;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param userID the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UserRepeatLoginException(String userID) {
        super(userID);
        this.userID = userID;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return "用户"+userID+"已登陆！";
    }
}
