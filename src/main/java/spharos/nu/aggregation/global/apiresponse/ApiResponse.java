package spharos.nu.aggregation.global.apiresponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonPropertyOrder({"status", "result", "message"})
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
	private Integer status;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;
	private String message;

	public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
		ApiResponse<T> response = new ApiResponse<>(HttpStatus.OK.value(), data, message);
		return ResponseEntity.ok(response);
	}

	public static <T> ResponseEntity<ApiResponse<T>> fail(Integer status, String message) {
		ApiResponse<T> response = new ApiResponse<>(status, null, message);
		return ResponseEntity.status(HttpStatus.valueOf(status)).body(response);
	}
}
