package com.example.apiserver.service;

/**
 * 이미지 삭제 정책
 * 
 * TODO: 이미지 삭제 정책 결정 필요
 * 
 * 옵션 1: IMMEDIATE_DELETE - 즉시 삭제
 *   - 게시글/프로필 삭제 시 S3에서 즉시 이미지 삭제
 *   - 장점: 저장 공간 절약
 *   - 단점: 복구 불가능
 * 
 * 옵션 2: DELAYED_DELETE - 지연 삭제
 *   - 게시글/프로필 삭제 시 DB에서만 삭제 표시, S3는 보관
 *   - 주기적으로 사용하지 않는 이미지 정리 (예: 30일 후 삭제)
 *   - 장점: 복구 가능, 실수 방지
 *   - 단점: 저장 공간 사용
 */
public enum ImageDeletionPolicy {
    /**
     * 즉시 삭제 정책
     * 게시글/프로필 삭제 시 S3에서 즉시 이미지 삭제
     */
    IMMEDIATE_DELETE,
    
    /**
     * 지연 삭제 정책
     * 게시글/프로필 삭제 시 DB에서만 삭제 표시, S3는 보관
     * 주기적으로 사용하지 않는 이미지 정리 필요
     */
    DELAYED_DELETE
}

