package spharos.nu.goods.domain.goods.dto.response;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoodsCodeDto {
	@Schema(description = "상품 코드")
	private final String goodsCode;

	@Builder
	@QueryProjection
	public GoodsCodeDto(String goodsCode) {
		this.goodsCode = goodsCode;
	}
}
