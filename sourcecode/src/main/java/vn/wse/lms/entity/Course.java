package vn.wse.lms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the course database table.
 * 
 */
@Entity
@Table(name="course")
@NamedQuery(name="Course.findAll", query="SELECT c FROM Course c")
public class Course extends AbstractAuditingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="COURSE_ID", unique=true, nullable=false)
	private Long courseId;
	
	@Column(length=45)
	private String category;

	@Column(name="COURSE_CODE", length=45)
	private String courseCode;
	
	@Column(name="SHORT_DESCRIPTION", length=2000)
	private String shortDescription;

	@JsonIgnore
	@Lob
	@Column(name="DESCRIPTION")
	private byte[] description;
	
	@Transient
	private String descriptionAsString;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	@Column(length=200)
	private String icon;

	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	@DateTimeFormat (pattern="dd/MM/yyyy")
	private Date startDate;

	@Column(length=200)
	private String title;
	
	/*@Column(name="TRAINEES", length=3000)
	private String traineeIds;*/

	//bi-directional many-to-one association to Section
	@OneToMany(mappedBy="course", cascade = { CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Section> sections;
	
	@Transient
	private Integer numberSections;
	
	//@Transient
	@OneToMany(mappedBy="course", cascade = { CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<CourseRegistration> trainees;
	
	@OneToMany(mappedBy="course", cascade = { CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<CourseTrainer> trainers;
	
	@Column(name="STATUS")
	private Boolean status;

	@Transient
	private long numberPassed;
	
	@Transient
	private long numberFailed;
	
	public Course() {
	}
	
	public Course(Long courseId, String courseCode, String shortDescription, String icon, String title) {
		super();
		this.courseId = courseId;
		this.courseCode = courseCode;
		this.shortDescription = shortDescription;
		this.icon = icon;
		this.title = title;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCourseCode() {
		return this.courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public byte[] getDescription() {
		return description;
	}

	public void setDescription(byte[] description) {
		this.description = description;
	}

	public String getDescriptionAsString() {
		return this.description != null ? new String(this.description) : "";
	}

	public void setDescriptionAsString(String descriptionAsString) {
		if(StringUtils.isNotBlank(descriptionAsString)){
			this.description = descriptionAsString.getBytes();
		} else{
			this.description = null;
		}
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Section> getSections() {
		return this.sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public Section addSection(Section section) {
		getSections().add(section);
		section.setCourse(this);

		return section;
	}

	public Section removeSection(Section section) {
		getSections().remove(section);
		section.setCourse(null);

		return section;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Integer getNumberSections() {
		return numberSections;
	}

	public void setNumberSections(Integer numberSections) {
		this.numberSections = numberSections;
	}
	
	public List<CourseRegistration> getTrainees() {
		return trainees;
	}

	public void setTrainees(List<CourseRegistration> trainees) {
		this.trainees = trainees;
	}

	public List<CourseTrainer> getTrainers() {
		return trainers;
	}

	public void setTrainers(List<CourseTrainer> trainers) {
		this.trainers = trainers;
	}

	public long getNumberPassed() {
		return numberPassed;
	}

	public void setNumberPassed(long numberPassed) {
		this.numberPassed = numberPassed;
	}

	public long getNumberFailed() {
		return numberFailed;
	}

	public void setNumberFailed(long numberFailed) {
		this.numberFailed = numberFailed;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public enum CourseStatus{
		ACTIVE(true),INACTIVE(false);
		
		private CourseStatus(boolean value) {
			this.value = value;
		}

		private boolean value;

		public boolean isValue() {
			return value;
		}
	}

}