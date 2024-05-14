package spharos.nu.member.utils.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.member.global.exception.CustomException;
import spharos.nu.member.global.exception.errorcode.ErrorCode;
import spharos.nu.member.utils.redis.RedisService;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtProvider {
	@Value("${jwt.key}")
	private String secretKey;
	@Value("${jwt.token.access-expire-time}")
	private Long accessExpireTime;
	@Value("${jwt.token.refresh-expire-time}")
	private Long refreshExpireTime;

	private final RedisService redisService;

	/**
	 * 시크릿 키(서명키) 생성
	 */
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	/**
	 * 토큰 생성
	 * role 유저 역할 (USER: 회원 / ADMIN: 어드민)
	 * @param uuid 유저 uuid
	 * @return AccessToken, RefreshToken (String)
	 */
	public JwtToken createToken(String uuid) {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		Claims claims = Jwts.claims().setSubject(uuid);
		String role = "USER";
		claims.put("role", role);

		Date now = new Date();

		JwtToken jwtToken = new JwtToken(
			Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + accessExpireTime))
				.signWith(key)
				.compact(),
			Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + accessExpireTime))
				.signWith(key)
				.compact());

		// Redis DB에 {uuid: refreshToken} 정보 저장
		redisService.saveRefreshToken(uuid, jwtToken.getRefreshToken());

		return jwtToken;
	}

	/**
	 * 토큰 검증
	 * @param token jwtToken
	 * @return true(유효) / false(X)
	 */
	public boolean verifyToken(String token) {
		try {
			// 토큰 파싱
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.NO_AUTHORITY);
		}
	}

	public JwtToken reIssueToken(String refreshToken) {
		try {
			verifyToken(refreshToken);
		} catch (CustomException e) {
			throw new CustomException(ErrorCode.NO_AUTHORITY);
		}

		String uuid = getUuid(refreshToken);
		String redisRefreshToken = redisService.getRefreshToken(uuid);

		if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
			throw new CustomException(ErrorCode.NO_AUTHORITY);
		}

		// 기존의 데이터를 삭제하고 새로 생성
		redisService.deleteRefreshToken(uuid);
		return createToken(uuid);
	}

	/**
	 * 토큰에 담겨있는 uuid
	 * @param refreshToken 리프레시토큰
	 * @return uuid
	 */
	private String getUuid(String refreshToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(secretKey).build()
				.parseClaimsJws(refreshToken)
				.getBody()
				.getSubject();
		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.NO_AUTHORITY);
		}
	}
}
