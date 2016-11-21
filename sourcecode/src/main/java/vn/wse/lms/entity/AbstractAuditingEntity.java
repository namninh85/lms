package vn.wse.lms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.wse.lms.bean.UserDetailsBean;

/**
 * Base abstract class for entities which will hold definitions for created,
 * last modified by and created, last modified by date.
 */
@MappedSuperclass
/* @EntityListeners(AuditingEntityListener.class) */
public abstract class AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 997769360564844237L;

	@CreatedBy
	@Column(name = "CREATED_BY", length = 200, nullable = false, updatable = false)
	@JsonIgnore
	private String createdBy;

	@CreatedDate
	@Column(name = "CREATED_DATE", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@LastModifiedBy
	@Column(name = "UPDATED_BY", length = 200)
	@JsonIgnore
	private String updatedBy;

	@LastModifiedDate
	@Column(name = "UPDATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date updatedDate;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@PrePersist
	public void prePersist() {
		String createdByUser = getUsernameOfAuthenticatedUser();
		this.createdBy = createdByUser;
		this.updatedBy = createdByUser;
		this.createdDate = new Date();
		this.updatedDate = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		String modifiedByUser = getUsernameOfAuthenticatedUser();
		this.updatedBy = modifiedByUser;
		this.updatedDate = new Date();
	}

	private String getUsernameOfAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		return ((UserDetailsBean) authentication.getPrincipal()).getUsername();
	}

}
