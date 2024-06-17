package spharos.nu.goods.domain.bid.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BidGoodsCodeDto {

	@Schema(description = "상품 코드")
	private final String goodsCode;

	@Builder
	@QueryProjection
	public BidGoodsCodeDto(String goodsCode) {
		this.goodsCode = goodsCode;
	}
}
