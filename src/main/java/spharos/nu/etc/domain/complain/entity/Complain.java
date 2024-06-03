package spharos.nu.etc.domain.complain.entity;

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
import spharos.nu.etc.global.entity.AuditBaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Table(name = "complain")
public class Complain extends AuditBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "complain_id", nullable = false)
	private Long id;

	@Column(name = "uuid", unique = true)
	private String uuid;

	@Column(name = "goods_code")
	private String goodsCode;

	@Column(name = "complain_status")
	private boolean complainStatus;

	@Column(name = "complain_reason")
	private String complainReason;

	@Column(name = "complain_content")
	private String complainContent;
}
