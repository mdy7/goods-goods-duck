package spharos.nu.goods.global.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import spharos.nu.goods.global.apiresponse.ApiResponse;
import spharos.nu.goods.global.exception.errorcode.ErrorCode;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

	/*
	미처 처리 못한 Exception 전부 이쪽으로
	 */
	@ExceptionHandler
	public ResponseEntity<Object> commonException(Exception e, WebRequest request) {
		// 확인용 Log
		log.info("No Handling Exception is = {}", e.getMessage());

		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		ResponseEntity<ApiResponse<Object>> responseBody = createResponseBody(errorCode);
		return super.handleExceptionInternal(
			e, responseBody, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	/*
    커스텀 예외 처리
     */
	@ExceptionHandler(value = CustomException.class)
	public ResponseEntity<Object> handleCustomException(CustomException e, WebRequest request) {
		// 확인용 Log
		log.info("error is {}", e.getErrorCode());

		ErrorCode errorCode = e.getErrorCode();
		ResponseEntity<ApiResponse<Object>> responseBody = createResponseBody(errorCode);

		return super.handleExceptionInternal(
			e,
			responseBody,
			HttpHeaders.EMPTY,
			HttpStatus.valueOf(errorCode.getStatus()),
			request);
	}

	private <T> ResponseEntity<ApiResponse<T>> createResponseBody(ErrorCode errorCode) {
		return ApiResponse.error(
			errorCode.getStatus(),
			errorCode.getMessage());
	}
}
