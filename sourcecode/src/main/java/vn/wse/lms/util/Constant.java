package vn.wse.lms.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value="Constant")
@Scope(value = "singleton")
public class Constant {
	
	public static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	public static final String APPLICATION_JSON_UTF8_VALUE = "application/json; charset=UTF-8"; 
	public static final String PREFIX_FILE_TEMP = "@@@_";
	public static final int PAGE_SIZE = 5;
	public static final String FOLDER_ID_GOOGLE_DRIVE_SHARE = "0B9Z9QLs6l0k2VlhsNW0yX3BqMTg";
	
	// question type
	public static final String QUESTION_SINGLE_CHOICE = "Single choice";
	public static final String QUESTION_MULTI_CHOICE = "Multiple choice";
	public static final String QUESTION_TEXT = "Text";
	public static final String QUESTION_ORDERING = "Ordering";

	public static final String STATUS_INPROGRESS = "In Progress";
	public static final String STATUS_COMPLETED = "Completed";
	
	public static final String QUIZ_RESULT_PASS = "PASS";
	public static final String QUIZ_RESULT_FAILED = "FAILED";
	
	public static final int QUESTION_LOW_RANK = 70;
			
}
