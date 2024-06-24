package spharos.nu.etc.domain.notice.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NoticeInfo {

	@Schema(description = "공지pk")
	private Long id;

	@Schema(description = "공지 제목")
	private String title;

	@Schema(description = "공지 내용")
	private String content;

	@Schema(description = "생성일자")
	private LocalDateTime createdAt;

	@Schema(description = "수정일자")
	private LocalDateTime  updatedAt;
}
