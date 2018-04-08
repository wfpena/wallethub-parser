package com.ef.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="blocked_ips")
public class BlockedIps {
	@Id
	@Column(name = "Id")
	@GeneratedValue
	private int Id;
	
	@Column(name = "reason")
	@Type(type="text")
	private String comment;
	
	@Column(name = "ip")
	private String ip;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
