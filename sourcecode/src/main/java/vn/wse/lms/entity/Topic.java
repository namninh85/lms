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
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * The persistent class for the topic database table.
 * 
 */
@Entity
@Table(name="topic")
@NamedQuery(name="Topic.findAll", query="SELECT t FROM Topic t")
public class Topic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TOPIC_ID", unique=true, nullable=false)
	private Long topicId;

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
	private Date startDate;

	@Column(length=200)
	private String title;

	//bi-directional many-to-one association to Material
	@OneToMany(mappedBy="topic", cascade = { CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Material> materials;

	@Column(name="SECTION_ID")
	private Long sectionId;
	
	@Column(name="LOCKED")
	private Boolean locked;
	
	@Formula("(select s.START_DATE from section s where s.SECTION_ID = SECTION_ID)")
	private Date sectionStartDate;
	
	@Formula("(select s.TO_DATE from section s where s.SECTION_ID = SECTION_ID)")
	private Date sectionTodate;
	
	@Transient
	private String belongSection;

	public Topic() {
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
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
		this.endDate = getSectionTodate();
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
		this.startDate = getSectionStartDate();
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

	public List<Material> getMaterials() {
		return this.materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}

	public Material addMaterial(Material material) {
		getMaterials().add(material);
		material.setTopic(this);

		return material;
	}

	public Material removeMaterial(Material material) {
		getMaterials().remove(material);
		material.setTopic(null);

		return material;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public Boolean getLocked() {
		if(this.locked == null)
			this.locked = false;
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Date getSectionStartDate() {
		return sectionStartDate;
	}

	public void setSectionStartDate(Date sectionStartDate) {
		this.sectionStartDate = sectionStartDate;
	}

	public Date getSectionTodate() {
		return sectionTodate;
	}

	public void setSectionTodate(Date sectionTodate) {
		this.sectionTodate = sectionTodate;
	}

	public String getBelongSection() {
		return belongSection;
	}

	public void setBelongSection(String belongSection) {
		this.belongSection = belongSection;
	}
	
	
}