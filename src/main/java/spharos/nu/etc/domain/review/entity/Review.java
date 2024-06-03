package spharos.nu.etc.domain.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import spharos.nu.etc.global.entity.CreatedAtBaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Table(name = "review")
public class Review extends CreatedAtBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id", nullable = false)
	private Long id;

	@Column(name = "seller_uuid", unique = true)
	private String sellerUuid;

	@Column(name = "bidder_uuid", unique = true)
	private String bidderUuid;

	@Column(name = "goods_code", nullable = false)
	private String goodsCode;

	@Column(name = "score", nullable = false)
	private Integer score;

	@Column(name = "content")
	private String content;
}
