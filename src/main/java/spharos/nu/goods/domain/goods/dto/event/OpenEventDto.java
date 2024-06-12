package spharos.nu.goods.domain.goods.dto.event;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class OpenEventDto {
	@Schema(description = "상품코드")
	private String goodsCode;
	@Schema(description = "경매시작시간")
	private LocalDateTime openedAt;
}
