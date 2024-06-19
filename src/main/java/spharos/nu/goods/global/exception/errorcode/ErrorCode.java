package spharos.nu.goods.global.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	// 400 : 잘못된 요청
	MISSING_PATH_VARIALBE(400,"pathVariable이 누락되었습니다"),
	MISSING_QUERY_PARAM(400,"QueryParameter이 누락되었습니다"),
	MISSING_BODY_REQUEST(400,"RequestBody가 누락되었습니다. 혹은 각 필드값이 적절한지 확인하세요"),
	INVALID_REQUEST_BODY(400, "RequestBody가 유효하지 않습니다"),
	INVALID_REQUEST_PARAM(400, "RequestParameter가 유효하지 않습니다"),
	INVALID_REQUEST_METHOD(400, "요청 메서드가 유효하지 않습니다"),
	TYPE_MISMATCH(400,"요청 데이터 중 유효하지 않은 타입이 있습니다."),
	WRONG_NUMBER(400, "인증번호가 일치하지 않습니다."),
	TOO_LOW_PRICE(400, "최소 입찰 가격보다 낮은 금액으로 입찰할 수 없습니다."),
	SELLER_CANNOT_BID(400, "판매자는 입찰할 수 없습니다."),
	BID_NOT_START(400, "경매 시작 전 입찰은 불가능합니다."),
	AFTER_BID_END(400, "경매 시간이 지난 경우 입찰 불가능합니다."),
	NO_BIDDER(400, "입찰자가 없습니다."),
	ALREADY_CANCELLED(400, "이미 취소된 경매입니다."),
	NOT_CLOSED(400, "경매가 마감되지 않았습니다."),
	INVALID_TRADING_STATUS(400, "현재 상품의 상태코드로 이 작업을 수행할 수 없습니다."),



	// 401 : 접근 권한이 없음
	NO_AUTHORITY(401, "접근 권한이 없습니다."),
	TOKEN_EXPIRED(401, "토큰이 만료되었습니다."),
	IS_NOT_SELLER(401, "판매자만 접근 가능한 경로입니다."),


	// 403: Forbidden
	PASSWORD_ERROR(403, "비밀번호가 일치하지 않습니다."),
	FORBIDDEN_DELETE(403, "이미 경매가 시작되어 삭제 불가능합니다"),

	// 404: 잘못된 리소스 접근
	NOT_FOUND_GATEWAY(404, "존재하지 않는 경로입니다."),
	NOT_FOUND_ENTITY(404, "해당 객체를 찾지 못했습니다."),
	NOT_FOUND_USER(404, "존재하지 않는 사용자입니다."),
	NOT_FOUND_GOODS(404, "존재하지 않는 상품입니다."),


	//409 : 중복된 리소스
	ALREADY_EXIST_USER(409, "이미 존재하는 사용자입니다."),
	WINNING_BID_ALREADY_EXIST(409, "이미 낙찰된 상품입니다."),



	//500 : INTERNAL SERVER ERROR
	INTERNAL_SERVER_ERROR(500, "서버 내부 에러입니다.");

	private final Integer status;
	private final String message;


}


