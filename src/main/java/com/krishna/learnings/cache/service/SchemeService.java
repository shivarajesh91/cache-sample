package com.krishna.learnings.cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishna.learnings.cache.dto.DateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchemeService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.external.api.url}")
    String apiUrl;

    Logger logger = LoggerFactory.getLogger(SchemeService.class);

    @Cacheable(value = "schemeCache", key = "#schemeId")
    public Map<String, Object> getSchemeData(int schemeId) throws JsonProcessingException {
        logger.info("SchemeService -> getSchemeData() here.");
        apiUrl = apiUrl + "/" + schemeId;
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        String jsonData = response.getBody();
        //logger.info(" API response body : {}", jsonData);
        Map<String, Object> data = objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});
        return data;
    }

    public Map<String, Object> filterData(Map<String, Object> data, String filter) {
        logger.info("SchemeService -> filterData() here.");
        Map<String, Object> response = new LinkedHashMap<>();

        Map<String, Object> meta = (Map<String, Object>) data.get("meta");
        response.put("fundHouse",meta.get("fund_house"));
        response.put("schemeCode",meta.get("scheme_code"));
        response.put("schemeName",meta.get("scheme_name"));
        //logger.info("meta response : {}", response);

        List<Map<String, String>> originalData = (List<Map<String, String>>) data.get("data");
        List<DateData> jsonData = objectMapper.convertValue(originalData, new TypeReference<List<DateData>>() {});

        LocalDate now = LocalDate.now();
        LocalDate startDate = getStartDate(now, filter);

        List<DateData> dateDataList = jsonData.stream()
                .filter(dateData -> LocalDate.parse(dateData.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).isAfter(startDate))
                .collect(Collectors.toList());

        //logger.info("Filtered Data : {}", dateDataList);
        List<String> dates = dateDataList.stream().map(DateData::getDate).toList();
        List<String> navs = dateDataList.stream().map(DateData::getNav).toList();
        //logger.info("Dates : {}", dates);
        //logger.info("navs : {}", navs);
        List<List<String>> filteredData = new ArrayList<>();
        filteredData.add(dates);
        filteredData.add(navs);
        //logger.info("Filtered Data List : {}", filteredData);
        response.put("data",filteredData);
        logger.info("response: {}", response);
        return response;
    }

    private LocalDate getStartDate(LocalDate now, String filter) {
        switch (filter){
            case "1W" -> {
                return now.minusWeeks(1);
            }
            case "1M" -> {
                return now.minusMonths(1);
            }
            case "1Y" -> {
                return now.minusYears(1);
            }
            case "5Y" -> {
                return now.minusYears(5);
            }
            default -> throw new IllegalArgumentException("Invalid filter : " + filter);
        }
    }
}

