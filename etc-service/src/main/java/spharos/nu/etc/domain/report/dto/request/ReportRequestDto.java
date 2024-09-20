package spharos.nu.etc.domain.report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequestDto {

	@Schema(description = "신고 이유")
	private String complainReason;

	@Schema(description = "신고 내용")
	private String complainContent;
}
