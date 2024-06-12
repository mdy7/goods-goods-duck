package spharos.nu.goods.domain.bid.dto.event;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CloseEventDto {
	@Schema(description = "상품코드")
	private String goodsCode;
	@Schema(description = "경매종료시간")
	private LocalDateTime closedAt;
	@Schema(description = "판매자uuid")
	private String sellerUuid;
}