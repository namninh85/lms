package vn.wse.lms.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import vn.wse.lms.entity.QuestionTemplate;


@Component
public class ScoreUtil {
	private static Logger logger = LoggerFactory.getLogger(ScoreUtil.class);
	
    public static long score(QuestionTemplate template, String ans) {
    	long grade = 0;
    	boolean result = true;
    	String[] rightAns = null;
    	if (template.getRightAnswer() != null) {
    		rightAns = template.getRightAnswer().split(",");
    	}
    	
    	Set<String> rightAnsList = ArrayUtil.toSet(rightAns);
    	if (ans != null) {
    		String[] ansArr  = ans.split(",");
    		Set<String> setAns = ArrayUtil.toSet(ansArr);
    		
    		if (setAns.equals(rightAnsList)) {
    			int d = new Double(template.getGrade()).intValue();
        		grade = d;
    		}
    	}
    	
    	return grade;
    	
    }
	
    public static long scoreList(QuestionTemplate template, String ans) {
    	long grade = 0;
    	boolean result = true;
    	String[] rightAns = null;
    	if (template.getRightAnswer() != null) {
    		rightAns = template.getRightAnswer().split(",");
    	}
    	
    	String rightAnsList = ArrayUtil.toString(rightAns);
    	if (ans != null) {
    		String[] ansArr  = ans.split(",");
    		String setAns = ArrayUtil.toString(ansArr);
    		
    		
    		if (setAns.equals(rightAnsList)) {
    			int d = new Double(template.getGrade()).intValue();
        		grade = d;
    		}
    	}
    	
    	return grade;
    	
    }
}
