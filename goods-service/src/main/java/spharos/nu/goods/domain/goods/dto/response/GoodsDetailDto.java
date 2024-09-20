package spharos.nu.goods.domain.goods.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import spharos.nu.goods.domain.goods.dto.request.ImageDto;
import spharos.nu.goods.domain.goods.dto.request.TagDto;

@Getter
@Builder
@ToString
public class GoodsDetailDto {
	@Schema(description = "거래상태")
	private byte tradingStatus;
	@Schema(description = "상품명")
	private String goodsName;
	@Schema(description = "상품설명")
	private String description;
	@Schema(description = "시작가격")
	private Long minPrice;
	@Schema(description = "경매시작시간")
	private LocalDateTime openedAt;
	@Schema(description = "경매마감시간")
	private LocalDateTime closedAt;
	@Schema(description = "선호거래방법")
	private byte wishTradeType;
	@Schema(description = "태그리스트")
	private List<TagDto> tags;
	@Schema(description = "이미지url리스트")
	private List<ImageDto> imageUrls;
}
