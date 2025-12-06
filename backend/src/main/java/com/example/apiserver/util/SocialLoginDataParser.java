package com.example.apiserver.util;

import com.example.apiserver.entity.Gender;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * 소셜 로그인 데이터 파싱 유틸리티
 * SRP 원칙 준수를 위한 유틸리티 클래스
 */
@Slf4j
public class SocialLoginDataParser {

    /**
     * 생년월일 파싱
     * @param birthYear 생년 (예: "1990")
     * @param birthDay 생일 (예: "01-01" 또는 "0101")
     * @return LocalDate 객체, 파싱 실패 시 null
     */
    public static LocalDate parseBirthDate(String birthYear, String birthDay) {
        if (birthYear == null || birthYear.isBlank()) {
            return null;
        }
        try {
            // 네이버: "1990", "01-01" 형식
            // 카카오: "1990", "0101" 형식
            if (birthDay != null && !birthDay.isBlank()) {
                String day = birthDay.replace("-", "");
                if (day.length() == 4) {
                    String month = day.substring(0, 2);
                    String date = day.substring(2, 4);
                    return LocalDate.parse(birthYear + "-" + month + "-" + date);
                }
            }
            return LocalDate.of(Integer.parseInt(birthYear), 1, 1);
        } catch (Exception e) {
            log.warn("생년월일 파싱 실패: {} {}", birthYear, birthDay);
            return null;
        }
    }

    /**
     * 성별 파싱
     * @param gender 성별 문자열 (네이버: "M", "F", 카카오: "male", "female")
     * @return Gender enum, 파싱 실패 시 null
     */
    public static Gender parseGender(String gender) {
        if (gender == null || gender.isBlank()) {
            return null;
        }
        // 네이버: "M", "F"
        // 카카오: "male", "female"
        if ("M".equalsIgnoreCase(gender) || "male".equalsIgnoreCase(gender)) {
            return Gender.MALE;
        } else if ("F".equalsIgnoreCase(gender) || "female".equalsIgnoreCase(gender)) {
            return Gender.FEMALE;
        }
        return null;
    }
}

