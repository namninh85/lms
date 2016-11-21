package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


/**
 * The persistent class for the answer_template database table.
 * 
 */
@Entity
@Table(name="answer_template")
@NamedQuery(name="AnswerTemplate.findAll", query="SELECT a FROM AnswerTemplate a")
public class AnswerTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ANSWER_TEMPLATE_ID", unique=true, nullable=false)
	private Long answerTemplateId;

	@Column(name="`INDEX`")
	private int index;

	@Column(length=200)
	private String text;

	//bi-directional many-to-one association to QuestionTemplate
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne
	@JoinColumn(name="QUESTION_TEMPLATE_ID")
	private QuestionTemplate questionTemplate;

	public AnswerTemplate() {
	}

	public Long getAnswerTemplateId() {
		return answerTemplateId;
	}

	public void setAnswerTemplateId(Long answerTemplateId) {
		this.answerTemplateId = answerTemplateId;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public QuestionTemplate getQuestionTemplate() {
		return this.questionTemplate;
	}

	public void setQuestionTemplate(QuestionTemplate questionTemplate) {
		this.questionTemplate = questionTemplate;
	}

	public static enum AnswerType {
		MULTIPLE_CHOICE("Multiple choice"), CHECKBOX("Checkbox"), TEXT("Text"), ORDERING("Ordering");
		private AnswerType(String stringValue) {
			this.stringValue = stringValue;
		}

		private String stringValue;

		public String getStringValue() {
			return stringValue;
		}

	}
}