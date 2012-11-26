package terry.tan.utils;

import java.util.Date;

public final class DateTime {
	/**
	 * Makes sure this class is not instantiable
	 */
	private DateTime(){}
	
	/**
	 * Gets the current date time.
	 * @return An instance of Date, representing current date time.
	 */
	public static Date now() {
		return new Date();
	}
	
	public static long currentTime() {
		return now().getTime();
	}
}