package vn.wse.lms.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import vn.wse.lms.util.ArrayUtil;

public class ApplicationException extends BaseRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5496100867391321688L;

	private String messageCode=null;
	
	private Object[] messageParams=null;

	public String getMessageCode() {
		return messageCode;
	}

	public Object[] getMessageParams() {
		return messageParams;
	}

	public ApplicationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		messageCode=arg0;
	}

	public ApplicationException(String arg0) {
		super(arg0);
		messageCode=arg0;
	}
	
	public ApplicationException(String errorCode, String errorMessage) {
		messageCode = errorMessage;
		setExceptionID(errorCode);
	}
	
	public ApplicationException(String arg0, Throwable arg1, Object[] params) {
		super(arg0, arg1);
		messageParams=params;
		messageCode=arg0;
	}

	public ApplicationException(String arg0,Object[] params) {
		super(arg0);
		messageParams=params;
		messageCode=arg0;
	}
	
	private String printParams(){
		if(messageParams == null){
			return null;
		}
		
		return ArrayUtil.toString(messageParams);
	}
	
	public void printStackTrace() {
        System.out.println("message code:"+messageCode+" ");
        System.out.println("message params:" + printParams() +" ");
        super.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        s.println("message code:"+messageCode+" ");
        s.println("message params:"+ printParams() +" ");
        super.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s) {
        s.println("message code:"+messageCode+" ");
        s.println("message params:"+ printParams() +" ");
        super.printStackTrace(s);
    }

    public String toString() {
        return "message code:" + messageCode+ " "+
               "message params:" + printParams() + " " + 
               super.toString();
    }
	
}
