package spharos.nu.goods.domain.goods.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GoodsSellResponseDto {

	@Schema(description = "현재 페이지")
	private Integer nowPage;

	@Schema(description = "최대 페이지")
	private Integer maxPage;

	@JsonProperty("isLast")
	@Getter(AccessLevel.NONE)
	@Schema(description = "마지막 페이지 여부")
	private boolean isLast;

	@Schema(description = "굿즈 목록")
	private List<GoodsInfoDto> goodsList;
}
