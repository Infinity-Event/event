package com.capgemini.event.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Query;
import com.capgemini.event.entities.Response;
import com.capgemini.event.exceptions.QueryNotFoundException;
import com.capgemini.event.exceptions.ResponseNotFoundException;
import com.capgemini.event.repositories.QueryRepo;
import com.capgemini.event.repositories.ResponseRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class ResponseServiceImpl implements ResponseService {
    
    private final ResponseRepo responseRepo;
    private final QueryRepo queryRepo;

    @Autowired
    public ResponseServiceImpl(ResponseRepo responseRepo, QueryRepo queryRepo) {
        this.responseRepo = responseRepo;
        this.queryRepo = queryRepo;
    }

    @Override
    public Response getResponseByQueryId(Long queryId) {
        log.info("Fetching response for Query ID: {}", queryId);
        Query query = queryRepo.findById(queryId).orElseThrow(()-> {
        	log.warn("No Query Found with ID: {}", queryId);
        	throw new QueryNotFoundException("No Query Found with Query Id: "+queryId);
        });
        return responseRepo.findByQuery(query)
                .orElseThrow(() -> {
                    log.warn("No response found for Query ID: {}", queryId);
                    return new ResponseNotFoundException("No response found for Query ID: " + query.getQueryId());
                });
    }

    @Override
    public Response createResponse(Response response, Long queryId) {
    	Query query = queryRepo.findById(queryId)
    			.orElseThrow(()-> new QueryNotFoundException("Query not found"));
    	query.setResponse(response);
    	response.setQuery(query);
        log.info("Creating new response for Query ID: {}", response.getQuery().getQueryId());
        Response savedResponse = responseRepo.save(response);
        log.debug("Response saved with ID: {}", savedResponse.getResponseId());
        return savedResponse;
    }

    @Override
    public Response getResponseById(Long responseId) {
        log.info("Fetching response by ID: {}", responseId);
        Optional<Response> optionalResponse = responseRepo.findById(responseId);
        if (optionalResponse.isPresent()) {
            log.debug("Response found: {}", optionalResponse.get());
        } else {
            log.warn("No response found with ID: {}", responseId);
        }
        return optionalResponse.orElseThrow(()->{
        	log.warn("No Response Found with Response ID: {}", responseId);
        	return new ResponseNotFoundException("No Reponse Found for Response ID: "+ responseId);
        });
    }

    @Override
    public List<Response> getAllResponses() {
        log.info("Fetching all responses");
        List<Response> responses = responseRepo.findAll();
        log.debug("Number of responses fetched: {}", responses.size());
        return responses;
    }

    @Override
    public void deleteResponse(Long responseId) {
        log.info("Deleting response with ID: {}", responseId);
        responseRepo.deleteById(responseId);
        log.debug("Response with ID: {} deleted", responseId);
    }
}
