package spharos.nu.goods.domain.goods.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoodsSummaryDto {
	@Schema(description = "상품 코드")
	private final String goodsCode;
	@Schema(description = "상품 썸네일")
	private final String thumbnail;
	@Schema(description = "상품명")
	private final String goodsName;
	@Schema(description = "시작가격")
	private final Long minPrice;
	@Schema(description = "입찰마감일시")
	private final LocalDateTime closedAt;
	@Schema(description = "조회수")
	private final Long viewsCount;
	@Schema(description = "좋아요수")
	private final Long wishCount;
	@Schema(description = "입찰수")
	private final Long biddingCount;
	@Schema(description = "거래상태")
	private final byte tradingStatus;

	@Builder
	@QueryProjection
	public GoodsSummaryDto(String goodsCode, String thumbnail, String goodsName, Long minPrice, LocalDateTime closedAt,
		Long viewsCount, Long wishCount, Long biddingCount
		, byte tradingStatus) {
		this.goodsCode = goodsCode;
		this.thumbnail = thumbnail;
		this.goodsName = goodsName;
		this.minPrice = minPrice;
		this.closedAt = closedAt;
		this.viewsCount = viewsCount;
		this.wishCount = wishCount;
		this.biddingCount = biddingCount;
		this.tradingStatus = tradingStatus;
	}
}
