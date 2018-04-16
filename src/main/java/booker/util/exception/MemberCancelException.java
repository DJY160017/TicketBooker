package booker.util.exception;

/**
 * Created by Harvey on 2017/3/11.
 */
public class MemberCancelException extends Exception {

    /**
     * 错误信息
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
    public MemberCancelException(String userID) {
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
        return userID+"已取消会员资格，无法登陆";
    }
}
