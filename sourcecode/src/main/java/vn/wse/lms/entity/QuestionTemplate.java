package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the question_template database table.
 * 
 */
@Entity
@Table(name="question_template")
@NamedQuery(name="QuestionTemplate.findAll", query="SELECT q FROM QuestionTemplate q")
public class QuestionTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="QUESTION_TEMPLATE_ID", unique=true, nullable=false)
	private Long questionTemplateId;

	private double grade;

	@Column(name="QUESTION_TYPE", length=45)
	private String questionType;

	@Column(length=1)
	private String required;

	@Column(name="RIGHT_ANSWER", length=200)
	private String rightAnswer;

	@Column(name="TRAINER_FEEDBACK", length=200)
	private String trainerFeedback;

	//bi-directional many-to-one association to AnswerTemplate
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="questionTemplate", cascade= CascadeType.ALL)
	private List<AnswerTemplate> answerTemplates;

	//bi-directional many-to-one association to QuizTemplate
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="QUIZ_TEMPLATE_ID")
	private QuizTemplate quizTemplate;
	
		
	@Column(name="SECTION_INDEX")
	private String sectionIndex;
	
	@Column(name="SECTION_TITLE")
	private String sectionTitle;

	@Column(name="VIDEO_LINK")
	private String videoLink;
	
	@Column(name="QUESTION_TITLE", length=250)
	private String questionTitle;
	
	@Column(name="QUESTION", length=2000)
	private String question;

	public QuestionTemplate() {
	}

	public Long getQuestionTemplateId() {
		return questionTemplateId;
	}

	public void setQuestionTemplateId(Long questionTemplateId) {
		this.questionTemplateId = questionTemplateId;
	}

	public double getGrade() {
		return this.grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public String getQuestionType() {
		return this.questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getRequired() {
		return this.required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getRightAnswer() {
		return this.rightAnswer;
	}

	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public String getTrainerFeedback() {
		return this.trainerFeedback;
	}

	public void setTrainerFeedback(String trainerFeedback) {
		this.trainerFeedback = trainerFeedback;
	}

	public List<AnswerTemplate> getAnswerTemplates() {
		return this.answerTemplates;
	}

	public void setAnswerTemplates(List<AnswerTemplate> answerTemplates) {
		this.answerTemplates = answerTemplates;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public AnswerTemplate addAnswerTemplate(AnswerTemplate answerTemplate) {
		getAnswerTemplates().add(answerTemplate);
		answerTemplate.setQuestionTemplate(this);

		return answerTemplate;
	}

	public AnswerTemplate removeAnswerTemplate(AnswerTemplate answerTemplate) {
		getAnswerTemplates().remove(answerTemplate);
		answerTemplate.setQuestionTemplate(null);

		return answerTemplate;
	}

	public QuizTemplate getQuizTemplate() {
		return this.quizTemplate;
	}

	public void setQuizTemplate(QuizTemplate quizTemplate) {
		this.quizTemplate = quizTemplate;
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

	
}