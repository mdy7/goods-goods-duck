package spharos.nu.goods.domain.bid.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class WinningEventDto {
	@Schema(description = "상품코드")
	private String goodsCode;
	@Schema(description = "판매자uuid")
	private String sellerUuid;
	@Schema(description = "낙찰자uuid")
	private String bidderUuid;
}