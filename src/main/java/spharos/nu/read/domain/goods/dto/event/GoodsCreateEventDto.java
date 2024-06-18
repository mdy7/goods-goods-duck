package spharos.nu.read.domain.goods.dto.event;

import java.time.LocalDateTime;
import java.util.List;

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
public class GoodsCreateEventDto {

	@Schema(description = "상품코드")
	private String goodsCode;
	@Schema(description = "카테고리id")
	private Long categoryId;
	@Schema(description = "판매자uuid")
	private String sellerUuid;
	@Schema(description = "상품명")
	private String name;
	@Schema(description = "최소가격")
	private Long minPrice;
	@Schema(description = "상품설명")
	private String description;
	@Schema(description = "경매시작시간")
	private LocalDateTime openedAt;
	@Schema(description = "경매마감시간")
	private LocalDateTime closedAt;
	@Schema(description = "선호거래방법")
	private byte wishTradeType;
	@Schema(description = "거래 상태")
	private byte tradingStatus;
	@Schema(description = "숨김 여부")
	private Boolean isDisable;
	@Schema(description = "생성일자")
	private LocalDateTime createdAt;
	@Schema(description = "수정일자")
	private LocalDateTime updatedAt;
	@Schema(description = "태그 리스트")
	private List<String> tagList;

}
