package vn.wse.lms.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

import vn.wse.lms.entity.AnswerTemplate;
import vn.wse.lms.entity.QuestionTemplate;
import vn.wse.lms.util.GoogleSpreadSheetUtil;

@Controller
@RequestMapping("/spreadsheet")
public class SpreadSheetController {

	@RequestMapping(value="/parser", method = RequestMethod.GET)
	@ResponseBody
	public  List<QuestionTemplate> parser(@RequestParam(required = false) String url){
		List<QuestionTemplate> questionTemplates = new ArrayList<QuestionTemplate>();	
		try {
			SpreadsheetService service = GoogleSpreadSheetUtil.getSpreadsheetService();
			List<WorksheetEntry> worksheetEntries = GoogleSpreadSheetUtil.findAllWorksheetEntry(service);
			
			//QuestionTemplate
			for (WorksheetEntry worksheetEntry : worksheetEntries) {
				URL listFeedUrl = worksheetEntry.getListFeedUrl();
				ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
			
				// Iterate through each row, printing its cell values.
			    for (ListEntry row : listFeed.getEntries()) {
			    	// Print the first column's cell value
			    	String firstColumn = row.getTitle().getPlainText();
			    
			    	if(firstColumn.equalsIgnoreCase("Question setup"))
			    		continue;
			      
			      int cellNum = 0;
			      QuestionTemplate question = new QuestionTemplate();
			      List<AnswerTemplate> answerTemplates = new ArrayList<AnswerTemplate>();
			      int indexAnswers = 0;
			      // Iterate over the remaining columns, and print each cell value
			      for (String tag : row.getCustomElements().getTags()) {
			    	  
			    	  String value = row.getCustomElements().getValue(tag);
			    	  if(cellNum == 0)
			    		  question.setQuestionTitle(value);
			    	  if(cellNum == 2)
			    		  question.setQuestion(value);
			    	  if(cellNum == 3)
			    		  question.setQuestionType(answerType(value));
			    	  
			    	  boolean flag =   cellNum > 3 && cellNum<=13;
			    	  if(flag){
			    		  if(StringUtils.isNotBlank(value)){
			    			  AnswerTemplate answerTemplate = new AnswerTemplate();
				    		  answerTemplate.setIndex(indexAnswers);
				    		  answerTemplate.setQuestionTemplate(question);
				    		  answerTemplate.setText(value);
				    		  
				    		  answerTemplates.add(answerTemplate);
				    		  indexAnswers++;
			    		  }
			    	  }
			    	  
			    	  if(cellNum == 14)
			    		  question.setRightAnswer(value);
			    	  
			    	  if(cellNum == 15)
			    		  question.setRequired(getRequired(value));
			    	  
			    	  if(cellNum == 16)
			    		  question.setGrade(StringUtils.isNotBlank(value)? Double.valueOf(value) : 0.0);
			    		 
			    	  if(cellNum == 17)
			    		  question.setTrainerFeedback(value);	
			    	  
			    	  question.setAnswerTemplates(answerTemplates);
			    	  cellNum++;
			      }
			      questionTemplates.add(question);
			    }
			  }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return questionTemplates;
	}
	
	public String answerType(String text){
		if(StringUtils.isNotBlank(text) && text.equalsIgnoreCase(AnswerTemplate.AnswerType.MULTIPLE_CHOICE.getStringValue()))
			return "1";
		if(StringUtils.isNotBlank(text) && text.equalsIgnoreCase(AnswerTemplate.AnswerType.CHECKBOX.getStringValue()))
			return "2";
		if(StringUtils.isNotBlank(text) && text.equalsIgnoreCase(AnswerTemplate.AnswerType.TEXT.getStringValue()))
			return "3";
		if(StringUtils.isNotBlank(text) && text.equalsIgnoreCase(AnswerTemplate.AnswerType.ORDERING.getStringValue()))
			return "4";
		return "";
	}
	
	public String getRequired(String text){
		if(StringUtils.isNotBlank(text) && text.equalsIgnoreCase("Yes"))
			return "1";
		if(StringUtils.isNotBlank(text) && text.equalsIgnoreCase("No"))
			return "0";
		
		return "1";
	}
	
}
