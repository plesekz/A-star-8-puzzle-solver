/**
 * A custom exception class.
 * Returned when the blank tile is to be moved off the 3x3 grid.
 */
public class CannotExecuteException extends Exception {

	public CannotExecuteException(String string) {
		super();
		problem = string;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3285625406588045139L;
	private String problem;
	
	public String getProblem() {
		return problem;
	}
}
