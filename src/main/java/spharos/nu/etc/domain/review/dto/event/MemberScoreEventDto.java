package spharos.nu.etc.domain.review.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MemberScoreEventDto {

	@Schema(description = "후기 수신자 uuid")
	private String receiverUuid;

	@Schema(description = "반영 점수")
	private Integer score;
}
