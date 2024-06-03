package spharos.nu.notification.global.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import spharos.nu.notification.global.exception.errorcode.ErrorCode;
import spharos.nu.notification.global.exception.erroresponse.ErrorResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

	/*
	미처 처리 못한 Exception 발생 시, 전부 여기서 에러 핸들링
	 */
	@ExceptionHandler
	public ResponseEntity<Object> handleCommonException(Exception e, WebRequest request) {
		// 확인용 Log
		log.info("No Handling Exception is = {}", e.getMessage());

		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, null);
		return super.handleExceptionInternal(
			e, responseBody, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	/*
    커스텀 Exception 발생 시, 에러 핸들링
     */
	@ExceptionHandler(value = CustomException.class)
	public ResponseEntity<Object> handleCustomException(CustomException e, WebRequest request) {
		// 확인용 Log
		log.info("error is {}", e.getErrorCode());

		ErrorCode errorCode = e.getErrorCode();
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, null);

		return super.handleExceptionInternal(
			e,
			responseBody,
			HttpHeaders.EMPTY,
			HttpStatus.valueOf(errorCode.getStatus()),
			request);
	}

	/*
    NoSuchElementException 발생 시, 에러 핸들링
    (orElseThrow()에서 Optional 객체가 비어있을 경우 발생하는 예외)
    */
	@ExceptionHandler(NoSuchElementException.class)
	protected ResponseEntity<Object> handleNoSuchElementException(
		NoSuchElementException e,
		@NonNull WebRequest request
	) {
		ErrorCode errorCode = ErrorCode.NOT_FOUND_ENTITY;
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, null);
		return super.handleExceptionInternal(e, responseBody, HttpHeaders.EMPTY,
			HttpStatus.NOT_FOUND, request);
	}

	/*
    MissingPathVariableException 발생 시, 에러 핸들링
    (PathVariable이 요청에 누락되었을 때 발생하는 예외)
     */
	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(
		@NonNull MissingPathVariableException e,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request) {

		String requestURI = request.getContextPath();
		ErrorCode errorCode = ErrorCode.MISSING_PATH_VARIALBE;
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, requestURI);
		return super.handleExceptionInternal(e, responseBody, headers,
			status, request);
	}

	/*
	MissingServletRequestParameterException 발생 시, 에러핸들링
    (필수 쿼리 파라미터가 요청에 누락되었을 때 발생하는 예외)
     */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
		@NonNull MissingServletRequestParameterException e,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request) {
		ErrorCode errorCode = ErrorCode.MISSING_QUERY_PARAM;
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, request.getParameterMap());
		return super.handleExceptionInternal(e, responseBody, headers, status, request);
	}

	/*
	NoHandlerFoundException 발생 시, 에러 핸들링
    (존재하지 않는 경로로 요청을 한 경우 발생하는 예외. 404 Not Found 상태)
     */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
		@NonNull NoHandlerFoundException e,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request) {
		ErrorCode errorCode = ErrorCode.NOT_FOUND_GATEWAY;
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, null);
		return super.handleExceptionInternal(e, responseBody, headers, status, request);
	}

	/*
    HttpMessageNotReadableException 발생 시, 에러 핸들링
    (바디가 없거나 문제가 있어서 파싱할 수 없을 경우 발생하는 예외)
    */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		@NonNull HttpMessageNotReadableException e,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request) {

		ErrorCode errorCode = ErrorCode.MISSING_BODY_REQUEST;
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, null);
		return super.handleExceptionInternal(e, responseBody, headers, status, request);
	}

	/*
	MethodArgumentNotValidException 발생 시, 에러 핸들링
	(요청 데이터가 엔티티의 유효성 검증을 통과하지 못했을 때 발생하는 예외. 유효성 검증은 @NotNull 등을 말합니다.)
	*/
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request) {
		Map<String, String> errorArgs = new LinkedHashMap<>();
		ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;

		e.getBindingResult()
			.getFieldErrors()
			.forEach(fieldError -> {
				String fieldName = fieldError.getField();
				String errorMessage = Optional.ofNullable(
						fieldError.getDefaultMessage())
					.orElse("");

				errorArgs.merge(fieldName, errorMessage,
					(existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", "
						+ newErrorMessage);
			});

		ErrorResponse<Object> responseBody = createResponseBody(errorCode, errorArgs);
		return super.handleExceptionInternal(
			e, responseBody, headers, status, request);
	}

	/*
    HttpRequestMethodNotSupportedException 발생 시, 에러 핸들링
    (요청 메서드가 잘못되었을 때 발생하는 예외)
    */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
		@NonNull HttpRequestMethodNotSupportedException e,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request
	) {
		ErrorCode errorCode = ErrorCode.INVALID_REQUEST_METHOD;
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, null);
		return super.handleExceptionInternal(e, responseBody, headers, status, request);
	}

	/*
    TypeMismatchException 발생 시, 에러 핸들링
    (요청데이터 타입 변환 시 발생하는 예외)
    */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(
		@NonNull TypeMismatchException e,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request
	) {
		Object[] args = new Object[] {e.getPropertyName(), e.getValue()};
		String defaultDetail = "Failed to convert '" + args[0] + "' with value: '" + args[1] + "'";
		ErrorCode errorCode = ErrorCode.TYPE_MISMATCH;
		ErrorResponse<Object> responseBody = createResponseBody(errorCode, defaultDetail);
		return super.handleExceptionInternal(e, responseBody, headers, status, request);
	}

	private <T> ErrorResponse<T> createResponseBody(ErrorCode errorCode, T data) {
		return new ErrorResponse<>(
			errorCode.getStatus(), data, errorCode.getMessage());
	}
}
