package spharos.nu.goods.domain.goods.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GoodsReadDto {
	@Schema(description = "거래상태")
	private byte tradingStatus;
	@Schema(description = "상품명")
	private String goodsName;
	@Schema(description = "상품설명")
	private String description;
	@Schema(description = "시작가격")
	private Long minPrice;
	@Schema(description = "입찰시작일시")
	private LocalDateTime openedAt;
	@Schema(description = "입찰마감일시")
	private LocalDateTime closedAt;
	@Schema(description = "진행시간")
	private byte durationTime;
	@Schema(description = "선호거래방법")
	private byte wishTradeType;
	@Schema(description = "태그리스트")
	private List<String> tags;
	@Schema(description = "이미지url리스트")
	private List<String> imageUrls;
	@Schema(description = "낙찰가")
	private Long winningPrice;
}
