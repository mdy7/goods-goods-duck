package spharos.nu.chat.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spharos.nu.chat.global.exception.errorcode.ErrorCode;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;
}
