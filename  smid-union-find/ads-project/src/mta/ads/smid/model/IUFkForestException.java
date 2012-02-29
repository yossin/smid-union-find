package mta.ads.smid.model;

/**
 * Base IUFkForest exception class
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public abstract class IUFkForestException extends Exception{
	private static final long serialVersionUID = -1647158491649459021L;

	/**
	 * @param message error message
	 */
	IUFkForestException(String message){
		super(message);
	}
	
	/**
	 * The Exception is thrown when a given element name exceeds the maximum element number (n) 
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	public static class NameOutOfRangeException extends IUFkForestException{
		private static final long serialVersionUID = -7155060852473255621L;
		/**
		 * @param name the name of the element
		 * @param max the maximum value allowed
		 */
		public NameOutOfRangeException(int name, int max) {
			super("element '"+name+"' is out of range. (should be between 0 and "+(max-1)+")");
		}
	}
}