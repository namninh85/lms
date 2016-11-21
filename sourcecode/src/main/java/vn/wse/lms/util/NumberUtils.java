package vn.wse.lms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NumberUtils {
	private static Logger logger = LoggerFactory.getLogger(NumberUtils.class);

	public static long defaultLongValue(Long value) {
		if (value == null) {
			return 0l;
		} else {
		return value.longValue();
		}
	}
}
