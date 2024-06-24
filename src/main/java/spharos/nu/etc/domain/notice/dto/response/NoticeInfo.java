package spharos.nu.etc.domain.notice.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NoticeInfo {

	private Long id;

	private String title;

	private String content;

	private LocalDateTime createdAt;

	private LocalDateTime  updatedAt;
}
