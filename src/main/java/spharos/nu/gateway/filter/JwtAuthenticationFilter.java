package spharos.nu.gateway.filter;


import java.util.Objects;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import spharos.nu.gateway.auth.JwtProvider;

@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

	private final JwtProvider jwtProvider;


	public JwtAuthenticationFilter(JwtProvider jwtProvider) {
		super(Config.class);
		this.jwtProvider = jwtProvider;
	}


	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// 토큰검증
			if (request.getHeaders().containsKey("Authorization")) {
				String token = Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0).substring(7).trim();
				if (jwtProvider.validateToken(token)) {
					// request.mutate().header("UserUuid", jwtProvider.getUuid(token)).build();
					return chain.filter(exchange);
				}
			}

			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return response.setComplete();
		};
	}

	public static class Config {

	}
}
