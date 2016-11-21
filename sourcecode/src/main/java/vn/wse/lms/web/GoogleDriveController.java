package vn.wse.lms.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.drive.DriveFile;
import org.springframework.social.google.api.drive.DriveFileQueryBuilder;
import org.springframework.social.google.api.drive.DriveFilesPage;
import org.springframework.social.google.api.drive.DriveOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.google.api.services.drive.Drive;

import vn.wse.lms.bean.QuizTemplateBean;
import vn.wse.lms.entity.AnswerTemplate;
import vn.wse.lms.entity.QuestionTemplate;
import vn.wse.lms.entity.QuizSection;
import vn.wse.lms.entity.QuizTemplate;
import vn.wse.lms.util.Constant;
import vn.wse.lms.util.GoogleSpreadSheetUtil;
import vn.wse.lms.util.Utils;

@Controller
@RequestMapping("/google")
public class GoogleDriveController {

	private Logger logger = LoggerFactory.getLogger(GoogleDriveController.class);
	@Autowired
	UserIdSource userIdSource;
	
	@Autowired
	Google google;
	
	private static int SEPARATE_ROW = 3;
	
	private static String QZ_NUM_SECTION="Number of sections";
	private static String QZ_PASS_GRADE="Passing grade";
	private static String QZ_TOTAL_POINT="Total points";
	private static String QZ_POINT_TO_PASS="Points to pass";
	private static String QZ_TIME_LIMIT="Time limit";
	private static String QZ_NUM_ATTEMP="Number of attempts";
	
	
	// for section setup
	private static String QS_INDEX="Section #";
	private static String QS_TITLE="Section title";
	private static String QS_VIDEO="Embed video link";
	
	// for section setup
	private static String QA_SECTION="Section";
	private static String QA_QUESTION_TITLE="Question Title";
	private static String QA_ANSWER_TYPE="Answer type";
	private static String QA_ANSWER="Answer ";
	private static String QA_ANSWER_1="Answer 1";
	private static String QA_ANSWER_2="Answer 2";
	private static String QA_ANSWER_3="Answer 3";
	private static String QA_ANSWER_4="Answer 4";
	private static String QA_ANSWER_5="Answer 5";
	private static String QA_ANSWER_6="Answer 6";
	private static String QA_ANSWER_7="Answer 7";
	private static String QA_ANSWER_8="Answer 8";
	private static String QA_ANSWER_9="Answer 9";
	private static String QA_ANSWER_10="Answer 10";
	
	private static String QA_RIGHT_ANSWER="Right answer";
	private static String QA_REQUIRED="Required";
	private static String QA_GRADE="Grade";
	private static String QA_INCORRECT_MESSAGE="Message if incorrect answer is chosen";
	
		
	public static Map<String, String> quizSetupMapHeader = new HashMap<String, String>();
	static {
		
		quizSetupMapHeader.put(QZ_NUM_SECTION, QZ_NUM_SECTION);
		quizSetupMapHeader.put(QZ_PASS_GRADE, QZ_PASS_GRADE);				
		quizSetupMapHeader.put(QZ_TOTAL_POINT, QZ_TOTAL_POINT);				
		quizSetupMapHeader.put(QZ_POINT_TO_PASS, QZ_POINT_TO_PASS);
		quizSetupMapHeader.put(QZ_TIME_LIMIT, QZ_TIME_LIMIT);
		quizSetupMapHeader.put(QZ_NUM_ATTEMP, QZ_NUM_ATTEMP);
		
		quizSetupMapHeader.put(QS_INDEX, QS_INDEX);
		quizSetupMapHeader.put(QS_TITLE, QS_TITLE);
		quizSetupMapHeader.put(QS_VIDEO, QS_VIDEO);	
		
		quizSetupMapHeader.put(QA_SECTION, QA_SECTION);
		quizSetupMapHeader.put(QA_QUESTION_TITLE, QA_QUESTION_TITLE);
		quizSetupMapHeader.put(QA_ANSWER_TYPE, QA_ANSWER_TYPE);
		quizSetupMapHeader.put(QA_ANSWER_1, QA_ANSWER_1);
		quizSetupMapHeader.put(QA_ANSWER_2, QA_ANSWER_2);
		quizSetupMapHeader.put(QA_ANSWER_3, QA_ANSWER_3);
		quizSetupMapHeader.put(QA_ANSWER_4, QA_ANSWER_4);
		quizSetupMapHeader.put(QA_ANSWER_5, QA_ANSWER_5);
		quizSetupMapHeader.put(QA_ANSWER_6, QA_ANSWER_6);
		quizSetupMapHeader.put(QA_ANSWER_7, QA_ANSWER_7);
		quizSetupMapHeader.put(QA_ANSWER_8, QA_ANSWER_8);
		quizSetupMapHeader.put(QA_ANSWER_9, QA_ANSWER_9);
		quizSetupMapHeader.put(QA_ANSWER_10, QA_ANSWER_10);
		
		quizSetupMapHeader.put(QA_RIGHT_ANSWER, QA_RIGHT_ANSWER);
		quizSetupMapHeader.put(QA_REQUIRED, QA_REQUIRED);
		quizSetupMapHeader.put(QA_GRADE, QA_GRADE);
		quizSetupMapHeader.put(QA_INCORRECT_MESSAGE, QA_INCORRECT_MESSAGE);
		
		
		
	}

	@RequestMapping(value = "/spreadsheets", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,	List<Map<String,String>>> getAllSpreadsheetsByTitle(@RequestParam(value = "title", required = false) String title,WebRequest request) {
		DriveFileQueryBuilder queryBuilder = google.driveOperations().driveFileQuery().fromPage(null);

		//queryBuilder.maxResultsNumber(10);
		queryBuilder.trashed(false);
		queryBuilder.mimeTypeIs("application/vnd.google-apps.spreadsheet");
		// queryBuilder.parentIs("appdata");

		if (StringUtils.isNotBlank(title)) {
			//queryBuilder.titleContains(title);
			queryBuilder.fullTextContains(title);
		}
		
		DriveFilesPage files = queryBuilder.getPage();
		
		Map<String,	List<Map<String,String>>> result = new HashMap<String,List<Map<String,String>>>();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for (DriveFile driveFile : files.getItems()) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("id", driveFile.getId());
			map.put("title", driveFile.getTitle());
			list.add(map);
		}
		result.put("results", list);
		return result;
	}
	
	@RequestMapping(value="/file/{fileId}", method=GET)
	@ResponseBody
	public DriveFile getFileById(@PathVariable String fileId,HttpServletResponse response) {
		DriveOperations driveOperations = google.driveOperations();
		DriveFile file = driveOperations.getFile(fileId);
	
		return	file;
	}
	
	@RequestMapping(value="/parser/file/{fileId}", method=GET)
	@ResponseBody
	public QuizTemplate sheetDetails(@PathVariable String fileId,HttpServletResponse response) {
		
		QuizTemplate quizTemplate = new QuizTemplate();	
		DriveOperations driveOperations = google.driveOperations();
		DriveFile file = driveOperations.getFile(fileId);
	
		try {
			quizTemplate =	parserContent(file);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return	quizTemplate;
	}
	

	private void initMap(Map<String, Integer> quizSetupMap, Map<String, Integer> quizSectionMap, Map<String, Integer>  quizAnswerMap) {
		//init
				quizSetupMap.put(QZ_NUM_SECTION, 100000);
				quizSetupMap.put(QZ_TOTAL_POINT, 100000);
				quizSetupMap.put(QZ_POINT_TO_PASS, 100000);
				quizSetupMap.put(QZ_TIME_LIMIT, 100000);
				quizSetupMap.put(QZ_NUM_ATTEMP, 100000);
				quizSetupMap.put(QZ_PASS_GRADE, 100000);
				
				quizSectionMap.put(QS_INDEX, 100000);
				quizSectionMap.put(QS_TITLE, 100000);
				quizSectionMap.put(QS_VIDEO, 100000);
				
				quizAnswerMap.put(QA_SECTION, 100000);
				quizAnswerMap.put(QA_QUESTION_TITLE, 100000);
				quizAnswerMap.put(QA_ANSWER_TYPE, 100000);
				quizAnswerMap.put(QA_ANSWER_1, 100000);
				quizAnswerMap.put(QA_ANSWER_2, 100000);
				quizAnswerMap.put(QA_ANSWER_3, 100000);
				quizAnswerMap.put(QA_ANSWER_4, 100000);
				quizAnswerMap.put(QA_ANSWER_5, 100000);
				quizAnswerMap.put(QA_ANSWER_6, 100000);
				quizAnswerMap.put(QA_ANSWER_7, 100000);
				quizAnswerMap.put(QA_ANSWER_8, 100000);
				quizAnswerMap.put(QA_ANSWER_9, 100000);
				quizAnswerMap.put(QA_ANSWER_10, 100000);
				
				quizAnswerMap.put(QA_RIGHT_ANSWER, 100000);
				quizAnswerMap.put(QA_REQUIRED, 100000);
				quizAnswerMap.put(QA_GRADE, 100000);
				quizAnswerMap.put(QA_INCORRECT_MESSAGE, 100000);
				
	}
	public QuizTemplate parserContent(DriveFile file) throws Exception {
		
		QuizTemplate quizTemplate = new QuizTemplate();
		
	  	List<QuestionTemplate> questionTemplates = new ArrayList<QuestionTemplate>();	
	
		if (file == null) {
			throw new Exception("File not found");
		}
		
		String exportUri = file.getExportLinks().get("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		
		if (exportUri != null) {

			// Download XLSX file from Google
			HttpEntity entity = GoogleSpreadSheetUtil.geHttpEntityFileFromDrive(exportUri, google);
			if (entity != null) {
				XSSFWorkbook wb = new XSSFWorkbook(entity.getContent());
				CreationHelper createHelper = wb.getCreationHelper();

				XSSFSheet sheet;
				String sheetName;
				
				sheet = wb.getSheetAt(0);
				sheetName = sheet.getSheetName();
		
				// Remove formulas from the race sheets
				Row headerRow = sheet.getRow(0);
				/**
				 * Quiz setup
				 * 
				 */
				//Header
				int startInd = 0;
				int endInd = sheet.getLastRowNum() + 1;
				
				// config
				int quizSetupInd = 0;
				Map<String, Integer> quizSetupMap = new HashMap<String, Integer>();
				
				
				
				int setionSetupInd = 0;
				Map<String, Integer> quizSectionMap = new HashMap<String, Integer>();
							
				
				int questionSetupInd = 0;
				Map<String, Integer> quizAnswerMap = new HashMap<String, Integer>();
				
				//init map
				initMap(quizSetupMap, quizSectionMap, quizAnswerMap );
				
				for (int i = 0; i < endInd; i++) {
					Row row = sheet.getRow(i);
					Cell cell = row.getCell(0);//0: should be add in properties file.
					String cellValue = getValue(cell);
					if (cellValue.indexOf("Quiz setup") >= 0) {
						quizSetupInd = i;
						//
						for (int column = 1; column < 8; column++) {
							Cell headers = row.getCell(column);
							if (isColumn(headers, QZ_NUM_SECTION)) {
								quizSetupMap.put(QZ_NUM_SECTION, column);
							}
							if (isColumn(headers, QZ_TOTAL_POINT)) {
								quizSetupMap.put(QZ_TOTAL_POINT, column);
							}
							if (isColumn(headers, QZ_POINT_TO_PASS)) {
								quizSetupMap.put(QZ_POINT_TO_PASS, column);
							}
							if (isColumn(headers, QZ_TIME_LIMIT)) {
								quizSetupMap.put(QZ_TIME_LIMIT, column);
							}
							if (isColumn(headers, QZ_NUM_ATTEMP)) {
								quizSetupMap.put(QZ_NUM_ATTEMP, column);
							}
							if (isColumn(headers, QZ_PASS_GRADE)) {
								quizSetupMap.put(QZ_PASS_GRADE, column);
							}
							
						}
					}
					if (cellValue.indexOf("Section setup") >= 0) {
						setionSetupInd = i;
						for (int column = 1; column < 8; column++) {
							Cell headers = row.getCell(column);
							if (isColumn(headers, QS_INDEX)) {
								quizSectionMap.put(QS_INDEX, column);
							}
							if (isColumn(headers, QS_TITLE)) {
								quizSectionMap.put(QS_TITLE, column);
							}
							if (isColumn(headers, QS_VIDEO)) {
								quizSectionMap.put(QS_VIDEO, column);
							}
						}
					}
					if (cellValue.indexOf("Question setup") >= 0) {
						questionSetupInd = i;
						for (int column = 1; column < 30; column++) {
							Cell headers = row.getCell(column);
							if (isColumn(headers, QA_SECTION)) {
								quizAnswerMap.put(QA_SECTION, column);
							}
							if (isColumn(headers, QA_QUESTION_TITLE)) {
								quizAnswerMap.put(QA_QUESTION_TITLE, column);
							}
							if (isColumn(headers, QA_ANSWER_TYPE)) {
								quizAnswerMap.put(QA_ANSWER_TYPE, column);
							}
							
							if (isColumn(headers, QA_ANSWER_1)) {
								quizAnswerMap.put(QA_ANSWER_1, column);
							}
							if (isColumn(headers, QA_ANSWER_2)) {
								quizAnswerMap.put(QA_ANSWER_2, column);
							}
							if (isColumn(headers, QA_ANSWER_3)) {
								quizAnswerMap.put(QA_ANSWER_3, column);
							}
							if (isColumn(headers, QA_ANSWER_4)) {
								quizAnswerMap.put(QA_ANSWER_4, column);
							}
							if (isColumn(headers, QA_ANSWER_5)) {
								quizAnswerMap.put(QA_ANSWER_5, column);
							}
							if (isColumn(headers, QA_ANSWER_6)) {
								quizAnswerMap.put(QA_ANSWER_6, column);
							}
							if (isColumn(headers, QA_ANSWER_7)) {
								quizAnswerMap.put(QA_ANSWER_7, column);
							}
							if (isColumn(headers, QA_ANSWER_8)) {
								quizAnswerMap.put(QA_ANSWER_8, column);
							}
							if (isColumn(headers, QA_ANSWER_9)) {
								quizAnswerMap.put(QA_ANSWER_9, column);
							}
							if (isColumn(headers, QA_ANSWER_10)) {
								quizAnswerMap.put(QA_ANSWER_10, column);
							}
							
							if (isColumn(headers, QA_RIGHT_ANSWER)) {
								quizAnswerMap.put(QA_RIGHT_ANSWER, column);
							}
							if (isColumn(headers, QA_REQUIRED)) {
								quizAnswerMap.put(QA_REQUIRED, column);
							}
							if (isColumn(headers, QA_GRADE)) {
								quizAnswerMap.put(QA_GRADE, column);
							}
							if (isColumn(headers, QA_INCORRECT_MESSAGE)) {
								quizAnswerMap.put(QA_INCORRECT_MESSAGE, column);
							}
						}
						
					}
					
				}
				
				
				/**
				 * End quiz setup
				 */
				quizTemplate = new QuizTemplate();
				Row quizTemplateRow = sheet.getRow(quizSetupInd + SEPARATE_ROW);
				Double passGrade = getDoubleValue(quizTemplateRow.getCell(quizSetupMap.get(QZ_PASS_GRADE)));
				quizTemplate.setPassingGrade(passGrade);
				quizTemplate.setTotalPoint(getDoubleValue(quizTemplateRow.getCell(quizSetupMap.get(QZ_TOTAL_POINT))).intValue());
				quizTemplate.setQuizTime(getDoubleValue(quizTemplateRow.getCell(quizSetupMap.get(QZ_TIME_LIMIT))).intValue());
				quizTemplate.setPointToPass(getDoubleValue(quizTemplateRow.getCell(quizSetupMap.get(QZ_POINT_TO_PASS))).intValue());
				quizTemplate.setNumAttempt(getDoubleValue(quizTemplateRow.getCell(quizSetupMap.get(QZ_NUM_ATTEMP))).intValue());
				
				/**
				 * parse section
				 */
				List<QuizSection>quizSections = new ArrayList<>();
				Map<Integer, QuizSection> sectionMap = new HashMap<Integer, QuizSection>();
				for (int rowNum = quizSetupInd + SEPARATE_ROW; rowNum < questionSetupInd; rowNum++) {
					QuizSection quizSection = new QuizSection();
					Row r = sheet.getRow(rowNum);
					Double sectionIndex = getDoubleValue(r.getCell(quizSectionMap.get(QS_INDEX)));
					if (sectionIndex != null && sectionIndex > 0) {
						int sectionIndexInd = sectionIndex.intValue();
						quizSection.setSectionIndex(String.valueOf(sectionIndexInd));
						quizSection.setSectionTitle(getValue(r.getCell(quizSectionMap.get(QS_TITLE))));
						quizSection.setVideoLink(getValue(r.getCell(quizSectionMap.get(QS_VIDEO))));
						sectionMap.put(sectionIndexInd, quizSection);
					}
					
					quizSections.add(quizSection);
				}
				quizTemplate.setQuizSections(quizSections);
				
				/**
				 *  Parse question template
				 */
				for (int rowNum = questionSetupInd + SEPARATE_ROW; rowNum < endInd; rowNum++) {
					//question
					QuestionTemplate question = new QuestionTemplate();
					List<AnswerTemplate> answerTemplates = new ArrayList<AnswerTemplate>();
					Row r = sheet.getRow(rowNum);
					String title = getValue(r.getCell(quizAnswerMap.get(QA_QUESTION_TITLE)));
					
					if (title != null && title.trim().length() > 0) {
						question.setQuestion(title);
						question.setQuestionTitle(title);
						question.setQuestionType(getValue(r.getCell(quizAnswerMap.get(QA_ANSWER_TYPE))));
						question.setRightAnswer(getValue(r.getCell(quizAnswerMap.get(QA_RIGHT_ANSWER))));
						question.setGrade(getDoubleValue(r.getCell(quizAnswerMap.get(QA_GRADE))));
						question.setTrainerFeedback(getValue(r.getCell(quizAnswerMap.get(QA_INCORRECT_MESSAGE))));
						String requiredTxt = getValue(r.getCell(quizAnswerMap.get(QA_REQUIRED)));
						question.setRequired(getRequired(requiredTxt));
						Double sectionIndex = getDoubleValue(r.getCell(quizAnswerMap.get(QA_SECTION)));
						if (sectionIndex != null && sectionIndex > 0) {
							question.setSectionIndex(String.valueOf(sectionIndex.intValue()));
							if (sectionMap.get(sectionIndex.intValue()) != null) {
								question.setSectionTitle(sectionMap.get(sectionIndex.intValue()).getSectionTitle());
								question.setVideoLink(sectionMap.get(sectionIndex.intValue()).getVideoLink());
							}
						}
						
						
						//Ans
						for (int ansInd = 1; ansInd <= 10; ansInd++) {
							String ans = getValue(r.getCell(quizAnswerMap.get(QA_ANSWER + ansInd)));
							if (ans != null && ans.trim().length() > 0) {
								AnswerTemplate answerTemplate = new AnswerTemplate();
					    		  answerTemplate.setIndex(ansInd);
					    		  //answerTemplate.setQuestionTemplate(question);
					    		  answerTemplate.setText(ans);				    		  
					    		  answerTemplates.add(answerTemplate);
							}
						}
						question.setAnswerTemplates(answerTemplates);
						questionTemplates.add(question);
					}
					
				}
				
				
		} else {
			throw new Exception("No Excel export found!");
		}}
		quizTemplate.setQuestionTemplates(questionTemplates);
		return quizTemplate;
	}

	
	private String getValue(Cell c) {
		String value = "";
		try {
			if (c == null) return value;
			if(c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				Double d = Double.valueOf(c.getNumericCellValue());
				String txt = String.valueOf(d.intValue());
				value = txt;
			}
			if(c.getCellType() == Cell.CELL_TYPE_STRING)
				value= c.getStringCellValue();
		} catch (Exception e) {
		}
		return value;
	}
	
	private Double getDoubleValue(Cell c) {
		Double value = 0d;
		try {
			if (c == null) return value;
			if(c.getCellType() == Cell.CELL_TYPE_FORMULA) {
				value = Double.valueOf(c.getNumericCellValue());
			}
			if(c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				value = c.getNumericCellValue();
			}
			if(c.getCellType() == Cell.CELL_TYPE_STRING) {
				value= Double.valueOf(c.getStringCellValue());
			}
		} catch (Exception e) {
		}
		return value;
	}
	
	private boolean isColumn(Cell c, String header) {
		String value = getValue(c);
		if (value != null) {
			if (value.equalsIgnoreCase(quizSetupMapHeader.get(header))) {
				return true;
			}
		}
		return false;
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
	
	@RequestMapping(value="/downloadfile/{fileId}", method=GET)
	public void downloadFile(@PathVariable String fileId, HttpServletResponse response) throws Exception {
		DriveFile file = google.driveOperations().getFile(fileId);
		
		if (file == null) {
			throw new Exception("File not found");
		}
		Drive driveService = GoogleSpreadSheetUtil.getDriveService(); 
		
		GoogleSpreadSheetUtil.dowloadFile(driveService ,file, google);
	}

}
