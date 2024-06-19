package spharos.nu.goods.domain.bid.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidTopPriceResponseDto {

    private Long bidId;
    private String bidderUuid;
    private Long price;

}
