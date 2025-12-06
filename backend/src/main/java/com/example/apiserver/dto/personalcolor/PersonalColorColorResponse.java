package com.example.apiserver.dto.personalcolor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalColorColorResponse {
    private Long id;
    private String name;
    private String hexCode;
    private String category;
}

