package spharos.nu.member.domain.member.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberScoreEventDto {

	@Schema(description = "후기 수신자 uuid")
	private String receiverUuid;

	@Schema(description = "후기 점수")
	private Integer score;
}
