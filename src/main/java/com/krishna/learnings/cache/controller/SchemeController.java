package com.krishna.learnings.cache.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.krishna.learnings.cache.dto.UserRequest;
import com.krishna.learnings.cache.dto.UserResponse;
import com.krishna.learnings.cache.service.SchemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SchemeController {

    @Autowired
    private SchemeService schemeService;

    Logger logger = LoggerFactory.getLogger(SchemeController.class);

    @PostMapping("/scheme")
    public ResponseEntity<UserResponse> getScheme(@RequestBody UserRequest request) throws JsonProcessingException {
        logger.info("SchemeController here.");
        int schemeId = (int) request.getRequest().get("schemeId");
        String filter = (String) request.getRequest().get("filter");
        Map<String, Object> apiResponse = schemeService.getSchemeData(schemeId);
        Map<String, Object> filteredData = schemeService.filterData(apiResponse,filter);
        //logger.info("filtered data : {}", filteredData);
        UserResponse response = new UserResponse();
        response.setResponse(filteredData);
        //logger.info("Controller response : {}", response);
        return ResponseEntity.ok(response);
    }
}