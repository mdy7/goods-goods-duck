package spharos.nu.goods.domain.goods.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradingCompleteEventDto {

	@Schema(description = "굿즈코드")
	private String goodsCode;
}
