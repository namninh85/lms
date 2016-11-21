package vn.wse.lms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import vn.wse.lms.service.SocialMediaService;

@Entity
@Table(name = "users")
@NamedQueries({ @NamedQuery(name = Users.FIND_BY_EMAIL, query = "select us from Users us where us.email = :email"),
		@NamedQuery(name = Users.FIND_BY_ID, query = "select us from Users us where us.userId = :id") })
public class Users implements java.io.Serializable {
	/** serialVersionUID. */

	public static final String FIND_BY_EMAIL = "Account.findByEmail";
	public static final String FIND_BY_ID = "Account.findById";

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "USER_TYPE")
	private int userType;
	
	@Column(name = "AVATAR")
	private String avatar;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "DOB")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dob;

	@Column(name = "SHORT_DESCRIPTION")
	private String shortDescription;

	@Column(name = "LAST_LOGIN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	@Column(name = "STATUS")
	private String status;

	@Enumerated(EnumType.STRING)
	@Column(name = "SIGNIN_PROVIDER", length = 20)
	private SocialMediaService signInProvider;

	@Transient
	private String center;
	@Transient
	private String finalComment;
	@Transient
	private String finalAssessment;
	
	@Transient
	private List<Role> rolesNeedSave;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SocialMediaService getSignInProvider() {
		return signInProvider;
	}

	public void setSignInProvider(SocialMediaService signInProvider) {
		this.signInProvider = signInProvider;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getFinalComment() {
		return finalComment;
	}

	public void setFinalComment(String finalComment) {
		this.finalComment = finalComment;
	}

	public String getFinalAssessment() {
		return finalAssessment;
	}

	public void setFinalAssessment(String finalAssessment) {
		this.finalAssessment = finalAssessment;
	}

	public List<Role> getRolesNeedSave() {
		return rolesNeedSave;
	}

	public void setRolesNeedSave(List<Role> rolesNeedSave) {
		this.rolesNeedSave = rolesNeedSave;
	}
	
	

}