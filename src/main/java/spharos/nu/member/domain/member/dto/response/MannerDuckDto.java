package spharos.nu.member.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MannerDuckDto {

	@Schema(description = "매너덕 상태(단계)")
	private Integer level;

	@Schema(description = "다음 매너덕까지 필요한 점수")
	private Integer leftPoint;
}
