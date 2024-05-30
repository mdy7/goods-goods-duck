package spharos.nu.notification.domain.fcm.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponseWrapper<T> {
    private int result;
    private int resultCode;
    private String resultMsg;
    private T data;
}