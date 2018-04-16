package booker.util.exception;

public class PriceException extends Exception{

	/**
	 * Returns the detail message string of this throwable.
	 *
	 * @return the detail message string of this {@code Throwable} instance
	 * (which may be {@code null}).
	 */
	@Override
	public String getMessage(){
		return "请输入正确的金额";
	}

}
