package com.example.apiserver.service;

import java.util.List;

/**
 * JSON 변환 인터페이스
 * DIP 원칙 준수를 위한 추상화
 */
public interface JsonConverter {
    /**
     * 객체를 JSON 문자열로 변환
     * @param obj 변환할 객체
     * @return JSON 문자열
     */
    String toJson(Object obj);

    /**
     * JSON 문자열을 객체로 변환
     * @param json JSON 문자열
     * @param clazz 변환할 클래스
     * @param <T> 타입
     * @return 변환된 객체
     */
    <T> T fromJson(String json, Class<T> clazz);

    /**
     * JSON 문자열을 리스트로 변환
     * @param json JSON 문자열
     * @param clazz 리스트 요소 클래스
     * @param <T> 타입
     * @return 변환된 리스트
     */
    <T> List<T> fromJsonArray(String json, Class<T> clazz);
}

