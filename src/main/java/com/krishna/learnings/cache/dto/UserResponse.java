package com.krishna.learnings.cache.dto;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserResponse {

    private Map<String, Object> response;
}
