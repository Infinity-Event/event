package com.capgemini.event.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "responses")
public class Response {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "response_id")
	private Long responseId;
	
	@NotBlank(message = "Response body must not be blank")
	@Size(max = 1000, message = "Response body must not exceed 1000 characters")
	@Column(name = "response_body")
	private String responseBody;
	
	@NotNull(message = "Response date must not be null")
	@Column(name = "response_date")
	private LocalDate responseDate;
	
	@OneToOne(mappedBy = "response", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JsonBackReference
	private Query query;

	public Response() {
	}
	

	public Response(Long responseId, String responseBody, LocalDate responseDate) {
		this.responseId = responseId;
		this.responseBody = responseBody;
		this.responseDate = responseDate;
	}


	public Long getResponseId() {
		return responseId;
	}

	public void setResponseId(Long responseId) {
		this.responseId = responseId;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public LocalDate getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(LocalDate responseDate) {
		this.responseDate = responseDate;
	}


	public Query getQuery() {
		return query;
	}


	public void setQuery(Query query) {
		this.query = query;
	}


	@Override
	public String toString() {
		return "Response [responseId=" + responseId + ", responseBody=" + responseBody + ", responseDate="
				+ responseDate + ", query=" + query + "]";
	}

	
	
	
}
