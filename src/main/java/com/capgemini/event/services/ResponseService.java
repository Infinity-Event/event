package com.capgemini.event.services;

import java.util.List;

import com.capgemini.event.entities.Response;

public interface ResponseService {

	Response createResponse(Response response, Long queryId);
    Response getResponseById(Long responseId);
    List<Response> getAllResponses();
    void deleteResponse(Long responseId);
    Response getResponseByQueryId(Long queryId);
}
