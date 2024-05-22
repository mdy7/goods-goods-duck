package spharos.nu.goods.domain.goods.dto;

import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoodsInfoDto {

	@Schema(description = "상품코드")
	private String goodsCode;

	@Schema(description = "상품썸네일")
	private String thumbnail;

	@Schema(description = "상품명")
	private String goodsName;

	@Schema(description = "시작가격")
	private Long minPrice;

	@Schema(description = "좋아요 여부")
	private boolean liked;

	@Schema(description = "상품거래상태")
	private byte tradingStatus;

	@Builder
	@QueryProjection
	public GoodsInfoDto(String goodsCode, String thumbnail, String goodsName, Long minPrice, boolean liked,
		byte tradingStatus) {

		this.goodsCode = goodsCode;
		this.thumbnail = thumbnail;
		this.goodsName = goodsName;
		this.minPrice = minPrice;
		this.liked = liked;
		this.tradingStatus = tradingStatus;
	}
}
