package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the trainee_quiz_detail database table.
 * 
 */
@Entity
@Table(name="trainee_quiz_detail")
@NamedQuery(name="TraineeQuizDetail.findAll", query="SELECT t FROM TraineeQuizDetail t")
public class TraineeQuizDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TRAINEE_QUIZ_DETAIL_ID")
	private Long traineeQuizDetailId;

	@Column(name="QUESTION_TEMPLATE_ID")
	private Long questionTemplateId;

	private Long result;

	private Long score;

	@Column(name="TRAINEE_ANWSER")
	private String traineeAnwser;
	
	@Column(name="QUESTION")
	private String question;
	
	@Column(name="QUESTION_TYPE", length=45)
	private String questionType;

	@Column(name="TRAINEE_QUIZ_ID")
	private Long traineeQuizId;

	@Column(name="SECTION_INDEX")
	private String sectionIndex;
	
	@Column(name="SECTION_TITLE")
	private String sectionTitle;

	@Column(name="VIDEO_LINK")
	private String videoLink;
	
	@Column(name="REQUIRED")
	private String required;
	
	public TraineeQuizDetail() {
	}

	public Long getTraineeQuizDetailId() {
		return this.traineeQuizDetailId;
	}

	public void setTraineeQuizDetailId(Long traineeQuizDetailId) {
		this.traineeQuizDetailId = traineeQuizDetailId;
	}

	public Long getQuestionTemplateId() {
		return this.questionTemplateId;
	}

	public void setQuestionTemplateId(Long questionTemplateId) {
		this.questionTemplateId = questionTemplateId;
	}

	public Long getResult() {
		return this.result;
	}

	public void setResult(Long result) {
		this.result = result;
	}

	public Long getScore() {
		return this.score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public String getTraineeAnwser() {
		return this.traineeAnwser;
	}

	public void setTraineeAnwser(String traineeAnwser) {
		this.traineeAnwser = traineeAnwser;
	}

	public Long getTraineeQuizId() {
		return this.traineeQuizId;
	}

	public void setTraineeQuizId(Long traineeQuizId) {
		this.traineeQuizId = traineeQuizId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getSectionIndex() {
		return sectionIndex;
	}

	public void setSectionIndex(String sectionIndex) {
		this.sectionIndex = sectionIndex;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	
}