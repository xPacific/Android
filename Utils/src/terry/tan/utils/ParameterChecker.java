package terry.tan.utils;

public final class ParameterChecker {
	/**
	 * Makes sure this class is not instantiable
	 */
	private ParameterChecker(){}
	
	/**
	 * Checks whether a parameter is null or not
	 * @param paramName The name of the parameter
	 * @param param The parameter itself
	 * @throws IllegalArgumentException if @param is null
	 */
	public static void checkNull(String paramName, Object param) {
		if (param == null) {
			throw new IllegalArgumentException(paramName + "cannot be null");
		}
	}
}