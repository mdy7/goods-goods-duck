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

	@Schema(description = "상품명")
	private final String name;

	@Schema(description = "입찰한 금액")
	private final Long price;

	@Schema(description = "썸네일")
	private final String thumbnail;

	@Builder
	@QueryProjection
	public BidGoodsCodeDto(String goodsCode, String name, Long price, String thumbnail) {
		this.goodsCode = goodsCode;
		this.name = name;
		this.price = price;
		this.thumbnail = thumbnail;
	}
}
