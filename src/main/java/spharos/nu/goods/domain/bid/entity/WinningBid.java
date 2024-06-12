package spharos.nu.goods.domain.bid.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import spharos.nu.goods.global.entity.CreatedAtBaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class WinningBid extends CreatedAtBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "winning_bid_id")
	private Long id;

	@Column(nullable = false)
	private String bidderUuid;

	@Column(nullable = false)
	private String sellerUuid;

	@Column(nullable = false, unique = true)
	private String goodsCode;

	@Column
	private Long winningPrice;


}
