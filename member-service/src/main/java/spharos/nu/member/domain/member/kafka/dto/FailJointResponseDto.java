
package spharos.nu.member.domain.member.kafka.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FailJointResponseDto {
	private String uuid;
}
