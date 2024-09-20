package spharos.nu.goods.domain.goods.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spharos.nu.goods.global.entity.AuditBaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Goods extends AuditBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "goods_id")
	private Long id;
	@NotNull
	private Long categoryId;
	@NotBlank
	private String sellerUuid;
	@NotBlank
	private String goodsCode;
	@NotBlank
	private String name;
	@NotNull
	private Long minPrice;
	@NotBlank
	private String description;
	@NotNull
	private LocalDateTime openedAt;
	@NotNull
	private LocalDateTime closedAt;
	@NotNull
	@Column(columnDefinition = "SMALLINT DEFAULT 0")
	private byte wishTradeType;
	@NotNull
	@Column(columnDefinition = "SMALLINT DEFAULT 0")
	private byte tradingStatus;
	@NotNull
	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isDisable;

}
