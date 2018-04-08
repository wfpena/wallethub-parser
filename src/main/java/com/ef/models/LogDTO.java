package com.ef.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
public class LogDTO {
	@Column(name="id")
	@Id
	private Long Id;
	@Column(name="ip")
	private String ip;
	@Column(name="accessDate")
	private Date accessDate;
	@Column(name="userAgent")
	private String userAgent;
	@Column(name="status")
	private String status;
	@Column(name="numOfRequests")
	private Long numOfRequests;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Date getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getNumOfRequests() {
		return numOfRequests;
	}

	public void setNumOfRequests(Long numOfRequests) {
		this.numOfRequests = numOfRequests;
	}
	
	
	
}
