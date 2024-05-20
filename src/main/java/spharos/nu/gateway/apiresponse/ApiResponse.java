package spharos.nu.gateway.apiresponse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonPropertyOrder({"status", "message"})
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
	private Integer status;
	private String message;
}
