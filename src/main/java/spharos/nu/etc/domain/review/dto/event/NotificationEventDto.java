package spharos.nu.etc.domain.review.dto.event;

import org.apache.commons.lang3.builder.HashCodeExclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotificationEventDto {

	@Schema(description = "알림제목")
	private String title;

	@Schema(description = "알림내용")
	private String content;

	@Schema(description = "후기 수신자")
	private String uuid;

	@Schema(description = "리다이렉션 페이지")
	private String link;
}
