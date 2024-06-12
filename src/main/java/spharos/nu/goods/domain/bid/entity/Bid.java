package spharos.nu.goods.domain.bid.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spharos.nu.goods.global.entity.CreatedAtBaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid extends CreatedAtBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bid_id")
	private Long id;

	@Column(nullable = false)
	private String bidderUuid;

	@Column(nullable = false)
	private String goodsCode;

	@Column(nullable = false)
	private Long price;

	@Builder
	public Bid(String bidderUuid, String goodsCode, Long price) {
		this.bidderUuid = bidderUuid;
		this.goodsCode = goodsCode;
		this.price = price;
	}
}
