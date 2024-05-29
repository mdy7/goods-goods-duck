package spharos.nu.goods.domain.goods.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;


@Component
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 409:
                return new IllegalStateException("이미 낙찰된 상품입니다.");
            default:
                return new Exception("서버 내부 에러입니다.");
        }

    }
}