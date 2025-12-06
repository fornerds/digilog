package com.example.apiserver.service.sort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 정렬 전략 인터페이스
 * OCP 원칙 준수를 위한 전략 패턴 적용
 */
public interface SortStrategy<T> {
    /**
     * 정렬된 결과를 반환
     * @param pageable 페이지 정보
     * @param search 검색어 (선택사항)
     * @param params 추가 파라미터들
     * @return 정렬된 Page 객체
     */
    Page<T> sort(Pageable pageable, String search, Object... params);
    
    /**
     * 이 전략이 지원하는 정렬 타입
     * @return 정렬 타입 (예: "createdAt", "updatedAt", "popularity")
     */
    String getSortType();
}

