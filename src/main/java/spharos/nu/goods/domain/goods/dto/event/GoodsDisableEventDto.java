package spharos.nu.goods.domain.goods.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDisableEventDto {

	@Schema(description = "상품코드")
	private String goodsCode;
	@Schema(description = "숨김여부")
	private Boolean isDisable;

}
