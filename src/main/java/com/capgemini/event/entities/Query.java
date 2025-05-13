package com.capgemini.event.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "queries")

public class Query {

	@Id
	@GeneratedValue
	private Long queryId;
	private String query;
	private String response;
	private String status;
	private LocalDate queryDate;
	private LocalDate responseDate;

	@ManyToOne
	private User user;
	private Long orgId;

	public Query() {

	}

	public Query(Long queryId, String query, String response, String status, LocalDate queryDate,
			LocalDate responseDate, User user, Long orgId) {
		super();
		this.queryId = queryId;
		this.query = query;
		this.response = response;
		this.status = status;
		this.queryDate = queryDate;
		this.responseDate = responseDate;
		this.user = user;
		this.orgId = orgId;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(LocalDate queryDate) {
		this.queryDate = queryDate;
	}

	public LocalDate getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(LocalDate responseDate) {
		this.responseDate = responseDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return "Query [queryId=" + queryId + ", query=" + query + ", response=" + response + ", status=" + status
				+ ", queryDate=" + queryDate + ", responseDate=" + responseDate + ", user=" + user + ", orgId=" + orgId
				+ "]";
	}

}
