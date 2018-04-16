package booker.util.exception;

public class VenueNotExistException extends Exception {

    /**
     * 错误信息
     */
    private String venueID;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param venueID the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public VenueNotExistException(String venueID) {
        super(venueID);
        this.venueID = venueID;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return venueID + "不存在";
    }
}
