
package spharos.nu.member.domain.member.kafka.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class JoinRequestDto {
	private String uuid;
	private String nickname;
	private String profileImage;
	private String favoriteCategory;
}
