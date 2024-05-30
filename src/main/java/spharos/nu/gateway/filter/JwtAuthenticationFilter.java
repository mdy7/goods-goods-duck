package spharos.nu.gateway.filter;


import java.util.Objects;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import spharos.nu.gateway.apiresponse.ApiResponse;
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

			if (!request.getHeaders().containsKey("Authorization")) {
				return handleException(exchange, HttpStatus.UNAUTHORIZED, "토큰이 없습니다.");
			}

			String token = Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0).replace("Bearer ", "");

			if (!jwtProvider.validateToken(token)) {
				return handleException(exchange, HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
			}

			String uuid = jwtProvider.getUuid(token);

			request.mutate().header("User-Uuid", uuid).build();

			return  chain.filter(exchange.mutate().request(request).build());
		};
	}

	public static class Config {

	}

	private Mono<Void> handleException(ServerWebExchange exchange, HttpStatus status, String error) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		ApiResponse<String> apiResponse = new ApiResponse<>(status.value(), error);

		ObjectMapper objectMapper = new ObjectMapper();
		byte[] data;
		try {
			data = objectMapper.writeValueAsBytes(apiResponse);
		} catch (JsonProcessingException e) {
			data = new byte[0];
		}

		DataBuffer buffer = response.bufferFactory().wrap(data);
		return response.writeWith(Mono.just(buffer)).then(Mono.empty());
	}
}
