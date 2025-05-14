package com.capgemini.event.entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "queries")
public class Query {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long queryId;
	
	@NotBlank(message = "Query body must not be blank")
	@Size(max = 1000, message = "Query body must not exceed 1000 characters")
	private String queryBody;

	@NotBlank(message = "Status must not be blank")
	private String status;
	
	@NotNull(message = "Query date must not be null")
	private LocalDate queryDate;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "response_id")
	private Response response;

	@ManyToOne(optional = false)
	@JoinColumn(name ="user_id")
	@NotNull(message = "User must be specified")
	private User user;

	public Query() {
	}

	public Query(Long queryId, String queryBody, String status, LocalDate queryDate, Response response, User user) {
		this.queryId = queryId;
		this.queryBody = queryBody;
		this.status = status;
		this.queryDate = queryDate;
		this.response = response;
		this.user = user;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public String getQueryBody() {
		return queryBody;
	}

	public void setQueryBody(String queryBody) {
		this.queryBody = queryBody;
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

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Query [queryId=" + queryId + ", queryBody=" + queryBody + ", status=" + status + ", queryDate="
				+ queryDate + ", response=" + response + ", user=" + user + "]";
	}
	
}
