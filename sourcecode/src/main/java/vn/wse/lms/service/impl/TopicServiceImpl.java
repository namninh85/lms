package vn.wse.lms.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.drive.DriveFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.gdata.util.common.base.StringUtil;

import vn.wse.lms.entity.AnswerTemplate;
import vn.wse.lms.entity.Material;
import vn.wse.lms.entity.QuestionTemplate;
import vn.wse.lms.entity.Topic;
import vn.wse.lms.repository.MaterialRepository;
import vn.wse.lms.repository.QuestionTemplateRepository;
import vn.wse.lms.repository.TopicRepository;
import vn.wse.lms.service.TopicService;
import vn.wse.lms.util.Constant;
import vn.wse.lms.util.GoogleSpreadSheetUtil;
import vn.wse.lms.util.Utils;

@Service("topicService")
@Transactional(readOnly=true)
public class TopicServiceImpl implements TopicService {

	private Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private MaterialRepository materialRepository;
	
	@Autowired
	private QuestionTemplateRepository questionTemplateRepository;
	
	@Autowired
	Google google;
	
	@Override
	public Topic findById(Long topicId) {
		Topic topic = topicRepository.findOne(topicId);
		if(topic.getMaterials() != null)
			Collections.sort(topic.getMaterials());
		return topicRepository.findOne(topicId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void save(Topic topic) throws Exception {
		String oldIcon ="";
		String newIcon = topic.getIcon();
		if (topic.getTopicId() != null) {
			Topic topicOld = topicRepository.findOne(topic.getTopicId());
			oldIcon = topicOld.getIcon();
		}
		
		if(topic.getMaterials() != null && topic.getMaterials().size() >0){
			//update marterials
			if(topic.getTopicId() != null)
				materialRepository.deleteMaterialNotInListByTopicId(topic.getMaterials(), topic.getTopicId());
			
			int index = 0;
			for (Material material : topic.getMaterials()) {
				material.setTopic(topic);
				material.setPosition(index);
				
				if(Material.MaterialType.QUIZ.getStringValue().equals(material.getType())){
					
					material.setQuizTemplate(material.getQuizTemplate());
					if(material.getQuizTemplate() != null && material.getQuizTemplate().getQuestionTemplates() != null){
						boolean isNewParser = true;
						for (QuestionTemplate questionTemplate : material.getQuizTemplate().getQuestionTemplates()) {
							if(questionTemplate.getQuestionTemplateId() != null)
								isNewParser = false;
						}
						
						if(isNewParser) {
							if(material.getMaterialId() != null){
								Long quizTemplateId = materialRepository.findQuizTemplateIdByMaterialId(material.getMaterialId());
								if(quizTemplateId != null){
									if(material.getQuizTemplate() != null){
										material.getQuizTemplate().setQuizTemplateId(quizTemplateId);
									}
									questionTemplateRepository.deleteQuestionTeamplateAndAnswerTemplateByQuizTeamplateId(quizTemplateId);
								}
							}
						}
						
						for (QuestionTemplate questionTemplate : material.getQuizTemplate().getQuestionTemplates()) {
							questionTemplate.setQuizTemplate(material.getQuizTemplate());
							if(questionTemplate.getAnswerTemplates() != null){
								for (AnswerTemplate answerTemplate : questionTemplate.getAnswerTemplates()) {
									answerTemplate.setQuestionTemplate(questionTemplate);
								}
							}
						}
					}
				}
				
				if(StringUtils.isNotBlank(material.getFileId())){
					String path = Utils.getFolderCourse();
					String fileName = material.getPath();
					
					if(!Utils.isFileExits(path, fileName)){
						
						Drive driveService = GoogleSpreadSheetUtil.getDriveService(); 
						DriveFile file = google.driveOperations().getFile(material.getFileId());
						
						if (file == null) {
							throw new Exception(material.getMaterialName()+" File not found");
						}
						
						double mb= file.getFileSize()/1024/1024;
						if(mb > 50)
							throw new Exception("File too large, choose file less than or equal 50Mb!");
						
						//download file from google drive
						String newFileName = GoogleSpreadSheetUtil.dowloadFile(driveService, file, google);
						
						material.setPath(newFileName);
					}
				}
				
				index++;
			}
		}else{
			if(topic.getTopicId() != null)
				materialRepository.deleteAllMaterialByTopicId(topic.getTopicId());
			
		}
		
		if(StringUtils.isNotBlank(newIcon) && !newIcon.equalsIgnoreCase(oldIcon) ){
			File fileOldIcon = new File(Utils.getFolderImg()+oldIcon);
			if(fileOldIcon.exists() && fileOldIcon.isFile())
				FileUtils.deleteQuietly(fileOldIcon);
			Utils.move(Constant.PREFIX_FILE_TEMP+newIcon, Utils.getFoldelTemp(), Utils.getFolderImg());
		}
		topicRepository.save(topic);

	}
	
}
