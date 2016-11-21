package vn.wse.lms.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
/**
 * The persistent class for the material database table.
 * 
 */
@Entity
@Table(name="material")
@NamedQuery(name="Material.findAll", query="SELECT m FROM Material m")
public class Material implements Serializable, Comparable<Material> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MATERIAL_ID", unique=true, nullable=false)
	private Long materialId;

	@JsonIgnore
	@Lob
	@Column(name="MATERIAL_DATA")
	private byte[] materialData;
	
	@Transient
	private String materialDataAsString;
	
	@Transient
	private TraineeMaterial traineeMaterial;
	

	@Column(name="MATERIAL_LENGTH")
	private int materialLength;

	@Column(name="MATERIAL_NAME", length=200)
	private String materialName;

	@Column(length=200)
	private String path;

	@Column(length=20)
	private String type;
	
	@Transient
	private String fileId;
	
	@Column(name="POSITION")
	private Integer position;

	//bi-directional many-to-one association to QuizTemplate
	//@JsonProperty(access = Access.WRITE_ONLY)
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="QUIZ_TEMPLATE_ID")
	private QuizTemplate quizTemplate;

	//bi-directional many-to-one association to Topic
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne()
	@JoinColumn(name="TOPIC_ID")
	private Topic topic;

	public Material() {
	}

	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public byte[] getMaterialData() {
		return this.materialData;
	}

	public void setMaterialData(byte[] materialData) {
		this.materialData = materialData;
	}
	
	public String getMaterialDataAsString() {
		return this.materialData != null ? new String(this.materialData) : "";
	}

	public void setMaterialDataAsString(String materialDataAsString) {
			if(StringUtils.isNotBlank(materialDataAsString)){
			this.materialData = materialDataAsString.getBytes();
		} else{
			this.materialData = null;
		}
	}

	public int getMaterialLength() {
		return this.materialLength;
	}

	public void setMaterialLength(int materialLength) {
		this.materialLength = materialLength;
	}

	public String getMaterialName() {
		return this.materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public QuizTemplate getQuizTemplate() {
		return this.quizTemplate;
	}

	public void setQuizTemplate(QuizTemplate quizTemplate) {
		this.quizTemplate = quizTemplate;
	}

	public Topic getTopic() {
		return this.topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	
	public enum MaterialType{
		TEXT("1"), FILE("2"), VIDEO("3"), QUIZ("4");
		
		private MaterialType(String stringValue) {
			this.stringValue = stringValue;
		}

		private String stringValue;

		public String getStringValue() {
			return stringValue;
		}

	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public TraineeMaterial getTraineeMaterial() {
		return traineeMaterial;
	}

	public void setTraineeMaterial(TraineeMaterial traineeMaterial) {
		this.traineeMaterial = traineeMaterial;
	}

	
	@Override
	public int compareTo(Material compareMaterial) {
		Integer compareQuantity = ((Material) compareMaterial).getPosition();
		
		if(this.position == null){
			return -1;
		}else if(compareQuantity == null){
			return 1;
		}
	
		return this.position - compareQuantity;
			
	}

}