package vn.wse.lms.exception;

public class SystemException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6788224272498102069L;

	public SystemException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SystemException(String arg0) {
		super(arg0);
	}

	public SystemException(Throwable arg0) {
		super(arg0);
	}

}
