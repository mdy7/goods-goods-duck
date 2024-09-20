package spharos.nu.gateway.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtProvider {
	@Value("${jwt.key}")
	private String secretKey;

	/**
	 * 토큰 검증
	 * @param token jwtToken
	 * @return true(유효) / false(X)
	 */
	public boolean validateToken(String token) {
		try {
			// 토큰 파싱
			log.info("토큰검증");
			Jwts.parserBuilder()
				.setSigningKey(Decoders.BASE64.decode(secretKey))
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.info("토큰검증 실패 : 토큰 만료");
			return false;
		} catch (Exception e) {
			log.info("토큰검증 실패 : 토큰 오류");
			return false;
		}
	}


	/**
	 * 토큰에 담겨있는 uuid
	 * @param refreshToken 리프레시토큰
	 * @return uuid
	 */
	public String getUuid(String refreshToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(Decoders.BASE64.decode(secretKey)).build()
				.parseClaimsJws(refreshToken)
				.getBody()
				.getSubject();
		} catch (ExpiredJwtException e) {
			throw new IllegalArgumentException("Token expired");
		} catch (Exception e) {
			throw new IllegalArgumentException("No authority");
		}
	}

	/**
	 * jwt claim 에 담긴 key-value 반환
	 * @param key token claim 에 설정되어 있는 key 값 이름
	 * @return 해당 key 있으면 value 반환 / 없으면 빈 문자열 반환
	 */
	public String getValue(String token, String key) {
		Claims claims = Jwts.parserBuilder().setSigningKey(Decoders.BASE64.decode(secretKey)).build().parseClaimsJws(token).getBody();
		if (claims.containsKey(key)) {
			return claims.get(key).toString();
		} else {
			return "";
		}
	}
}
