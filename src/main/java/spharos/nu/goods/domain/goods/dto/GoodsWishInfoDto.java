package spharos.nu.goods.domain.goods.dto;

import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoodsWishInfoDto {

	@Schema(description = "상품코드")
	private String goodsCode;

	@Schema(description = "상품썸네일")
	private String thumbnail;

	@Schema(description = "상품명")
	private String goodsName;

	@Builder
	@QueryProjection
	public GoodsWishInfoDto(String goodsCode, String thumbnail, String goodsName) {

		this.goodsCode = goodsCode;
		this.thumbnail = thumbnail;
		this.goodsName = goodsName;
	}
}
