package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the quiz_section database table.
 * 
 */
@Entity
@Table(name="quiz_section")
@NamedQuery(name="QuizSection.findAll", query="SELECT q FROM QuizSection q")
public class QuizSection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="QUIZ_SECTION_ID")
	private Long quizSectionId;

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

	public QuizSection() {
	}

	public Long getQuizSectionId() {
		return this.quizSectionId;
	}

	public void setQuizSectionId(Long quizSectionId) {
		this.quizSectionId = quizSectionId;
	}

	
	public QuizTemplate getQuizTemplate() {
		return quizTemplate;
	}

	public void setQuizTemplate(QuizTemplate quizTemplate) {
		this.quizTemplate = quizTemplate;
	}

	public String getSectionTitle() {
		return this.sectionTitle;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

	public String getVideoLink() {
		return this.videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getSectionIndex() {
		return sectionIndex;
	}

	public void setSectionIndex(String sectionIndex) {
		this.sectionIndex = sectionIndex;
	}

	
}