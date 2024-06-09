package spharos.nu.aggregation.domain.aggregation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Table(name = "wish_count")
public class WishCount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wish_count_id")
	private Long id;

	@NotBlank
	@Column(name = "goods_code")
	private String goodsCode;

	@NotNull
	private Long count;


	public void increaseWishCount() {
		this.count++;
	}

	public void decreaseWishCount() {
		this.count--;
	}
}
