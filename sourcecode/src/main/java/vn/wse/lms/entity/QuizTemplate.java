package vn.wse.lms.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;


/**
 * The persistent class for the quiz_template database table.
 * 
 */
@Entity
@Table(name="quiz_template")
@NamedQuery(name="QuizTemplate.findAll", query="SELECT q FROM QuizTemplate q")
public class QuizTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="QUIZ_TEMPLATE_ID", unique=true)
	private Long quizTemplateId;

	@Column(name="PASSING_GRADE")
	private double passingGrade;

	@Column(name="QUIZ_TIME")
	private Integer quizTime;
	
	@Column(name="POINT_TO_PASS")
	private Integer pointToPass;
	
	@Column(name="NUM_ATTEMPT")
	private Integer numAttempt;

	@Column(length=500)
	private String title;

	@Column(name="TOTAL_POINT")
	private double totalPoint;

	//bi-directional many-to-one association to Material
/*	@OneToMany(mappedBy="quizTemplate", cascade = { CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Material> materials;*/
	
	
	//bi-directional many-to-one association to QuestionTemplate
	@OneToMany(mappedBy="quizTemplate", cascade = { CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<QuestionTemplate> questionTemplates;
	
	@OneToMany(mappedBy="quizTemplate", cascade = { CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<QuizSection> quizSections;

	public QuizTemplate() {
	}

	public Long getQuizTemplateId() {
		return quizTemplateId;
	}

	public void setQuizTemplateId(Long quizTemplateId) {
		this.quizTemplateId = quizTemplateId;
	}

	public double getPassingGrade() {
		return this.passingGrade;
	}

	public void setPassingGrade(double passingGrade) {
		this.passingGrade = passingGrade;
	}

	public Integer getQuizTime() {
		return this.quizTime;
	}

	public void setQuizTime(Integer quizTime) {
		this.quizTime = quizTime;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

/*	public List<Material> getMaterials() {
		return this.materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}
*/
	/*public Material addMaterial(Material material) {
		getMaterials().add(material);
		material.setQuizTemplate(this);

		return material;
	}

	public Material removeMaterial(Material material) {
		getMaterials().remove(material);
		material.setQuizTemplate(null);

		return material;
	}

	*/
	
	
	
	public List<QuestionTemplate> getQuestionTemplates() {
		return this.questionTemplates;
	}

	public List<QuizSection> getQuizSections() {
		return quizSections;
	}

	public void setQuizSections(List<QuizSection> quizSections) {
		this.quizSections = quizSections;
	}

	public void setQuestionTemplates(List<QuestionTemplate> questionTemplates) {
		this.questionTemplates = questionTemplates;
	}

	

	public Integer getPointToPass() {
		return pointToPass;
	}

	public void setPointToPass(Integer pointToPass) {
		this.pointToPass = pointToPass;
	}

	public double getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(double totalPoint) {
		this.totalPoint = totalPoint;
	}

	public Integer getNumAttempt() {
		return numAttempt;
	}

	public void setNumAttempt(Integer numAttempt) {
		this.numAttempt = numAttempt;
	}

	/*public QuestionTemplate addQuestionTemplate(QuestionTemplate questionTemplate) {
		getQuestionTemplates().add(questionTemplate);
		questionTemplate.setQuizTemplate(this);

		return questionTemplate;
	}

	public QuestionTemplate removeQuestionTemplate(QuestionTemplate questionTemplate) {
		getQuestionTemplates().remove(questionTemplate);
		questionTemplate.setQuizTemplate(null);

		return questionTemplate;
	}*/
	
	

}