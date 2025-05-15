 package com.capgemini.event.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.capgemini.event.entities.Response;
import com.capgemini.event.repositories.ResponseRepo;

@Service
public class ResponseServiceImpl implements ResponseService{

    private ResponseRepo responseRepo;

    public ResponseServiceImpl(ResponseRepo responseRepo) {
    	this.responseRepo = responseRepo;
    }
    
    @Override
    public Response createResponse(Response response) {
        return responseRepo.save(response);
    }

    @Override
    public Response getResponseById(Long responseId) {
        Optional<Response> optionalResponse = responseRepo.findById(responseId);
        return optionalResponse.orElse(null); // Or throw exception if preferred
    }

    @Override
    public List<Response> getAllResponses() {
        return responseRepo.findAll();
    }

    @Override
    public void deleteResponse(Long responseId) {
    	
        responseRepo.deleteById(responseId);
    }
}
