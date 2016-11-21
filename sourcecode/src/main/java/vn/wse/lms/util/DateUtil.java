package vn.wse.lms.util;

/**
 * contains all utilities related date type
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;


public class DateUtil {
    private static SimpleDateFormat df = new SimpleDateFormat(Constant.DEFAULT_DATE_FORMAT);

    public static String formatDate(Date date) {        
        return df.format(date);
    }
    

    public static Date parseDate(String dateStr) {
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Date parseDate(String dateStr, String pattern) {
    	if(StringUtils.isBlank(dateStr)) return null;
        try {
        	SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dateStr);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
        
    public static String parseToString(Date date, String pattern) {
        try {
        	SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } catch (Exception e) {
           // e.printStackTrace();
        }
        return null;
    }

    public static Integer parseDateToLong(Date bizDate, String pattern) {
    	if (bizDate == null) {
            return 0;
        }
        String sTemp =	(pattern == null || pattern.length() == 0) ? Constant.DEFAULT_DATE_FORMAT : pattern.trim();
        java.text.SimpleDateFormat df = new SimpleDateFormat(sTemp);
        return Integer.parseInt(df.format(bizDate));
    }
    
    public static Date getCurrentDate() {
    	return Calendar.getInstance().getTime();
    }

}
