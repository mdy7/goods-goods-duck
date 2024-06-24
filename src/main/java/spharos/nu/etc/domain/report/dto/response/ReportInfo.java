package spharos.nu.etc.domain.report.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReportInfo {

	@Schema(description = "신고자uuid")
	private String reporterUuid;

	@Schema(description = "신고 대상")
	private String subject;

	@Schema(description = "처리 상태")
	private boolean complainStatus;

	@Schema(description = "신고 사유")
	private String complainReason;

	@Schema(description = "신고 내용")
	private String complainContent;

	@Schema(description = "생성일자")
	private LocalDateTime createdAt;
}
