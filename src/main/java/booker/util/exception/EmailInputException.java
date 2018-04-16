package booker.util.exception;

public class EmailInputException extends Exception{

	/**
	 * Returns the detail message string of this throwable.
	 *
	 * @return the detail message string of this {@code Throwable} instance
	 * (which may be {@code null}).
	 */
	@Override
	public String getMessage(){
		return "邮箱格式不符合规范";
	}

}
