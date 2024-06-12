package spharos.nu.goods.domain.goods.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GoodsSummaryDto {
	@Schema(description = "썸네일Url")
	private String thumbnailUrl;
	@Schema(description = "상품명")
	private String goodsName;
	@Schema(description = "시작가격")
	private Long minPrice;
	@Schema(description = "경매시작시간")
	private LocalDateTime openedAt;
	@Schema(description = "경매마감시간")
	private LocalDateTime closedAt;
	@Schema(description = "거래상태")
	private byte tradingStatus;
}
