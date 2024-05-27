package spharos.nu.member.domain.member.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DuckPointInfoDto {

	@Schema(description = "변동금액")
	private Long changeAmount;

	@Schema(description = "잔여포인트")
	private Long leftPoint;

	@Schema(description = "입출금여부")
	private boolean changeStatus;

	@Schema(description = "내역상세")
	private String historyDetail;

	@Schema(description = "생성날짜")
	private LocalDateTime createdAt;
}
