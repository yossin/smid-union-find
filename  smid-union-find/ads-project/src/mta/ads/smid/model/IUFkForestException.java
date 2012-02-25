package mta.ads.smid.model;

public abstract class IUFkForestException extends Exception{
	private static final long serialVersionUID = -1647158491649459021L;

	IUFkForestException(String error){
		super(error);
	}
	
	public static class NameOutOfRangeException extends IUFkForestException{
		private static final long serialVersionUID = -7155060852473255621L;
		public NameOutOfRangeException(int name, int max) {
			super("element '"+name+"' is out of range. (should be between 0 and "+(max-1)+")");
		}
	}
}