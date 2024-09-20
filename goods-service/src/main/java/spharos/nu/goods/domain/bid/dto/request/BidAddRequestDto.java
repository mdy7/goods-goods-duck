package spharos.nu.goods.domain.bid.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidAddRequestDto {
	private Long price; // 입찰 가격
}
