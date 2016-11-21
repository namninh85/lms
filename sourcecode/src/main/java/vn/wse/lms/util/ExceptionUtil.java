package vn.wse.lms.util;

import vn.wse.lms.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public class ExceptionUtil {
	
	public static ApplicationException generateApplicationException(Exception exception, Class clazz) {
		ApplicationException appException = new ApplicationException(exception.getMessage());
		//Logger logger = LoggerFactory.getLogger(clazz);
		//logger.error("{} - {}", appException.getExceptionID(), appException.getMessage());
		return appException;
	}
	
	public static ApplicationException generateApplicationException(String errorCode, String errorMessage, Class clazz) {
		ApplicationException applicationException = new ApplicationException(errorCode, errorMessage);
		//Logger logger = LoggerFactory.getLogger(clazz);
		//logger.error("{} - {}", errorCode, errorMessage);
		return applicationException;
	}
}
