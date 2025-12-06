package com.example.apiserver.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequest {

    @Getter
    @NoArgsConstructor
    public static class Create {
        @NotBlank(message = "댓글 내용은 필수입니다")
        @Schema(description = "댓글 내용", example = "좋은 게시글입니다!")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class Update {
        @NotBlank(message = "댓글 내용은 필수입니다")
        @Schema(description = "댓글 내용", example = "수정된 댓글 내용입니다")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateAdmin {
        @NotNull(message = "게시글 ID는 필수입니다")
        private Long postId;

        @NotNull(message = "사용자 ID는 필수입니다")
        private Long userId;

        @NotBlank(message = "댓글 내용은 필수입니다")
        private String content;
    }
}

