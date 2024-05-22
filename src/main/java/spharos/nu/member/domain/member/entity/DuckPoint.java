package spharos.nu.member.domain.member.entity;

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
import spharos.nu.member.global.entity.AuditBaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Table(name = "duck_point")
public class DuckPoint extends AuditBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "duck_point_id")
	private Long id;

	@Column(nullable = false)
	private String uuid;

	@Column(name = "now_point")
	private Long nowPoint;
}
