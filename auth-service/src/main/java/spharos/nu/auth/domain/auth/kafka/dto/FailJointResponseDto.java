
package spharos.nu.auth.domain.auth.kafka.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FailJointResponseDto {
	private String uuid;
}
