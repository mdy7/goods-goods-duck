package spharos.nu.member.utils.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@AllArgsConstructor
public class JwtToken {
	String accessToken;
	String refreshToken;
}
