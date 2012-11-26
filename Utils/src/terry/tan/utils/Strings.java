package terry.tan.utils;

public final class Strings {
	/**
	 * Makes sure this class is not instantiable
	 */
	private Strings(){}
	
	/**
	 * Checks whether a string variable is null or empty.
	 * @param str The string variable to be checked
	 * @return A boolean indicating whether the inputed string variable is null or empty
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	public static boolean isNullOrWhitespace(String str) {
		return isNullOrEmpty(str) || str.trim().isEmpty();
	}
}