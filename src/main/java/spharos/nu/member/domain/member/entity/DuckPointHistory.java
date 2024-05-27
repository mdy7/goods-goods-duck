package spharos.nu.member.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import spharos.nu.member.global.entity.CreatedAtBaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "duck_point_history")
public class DuckPointHistory extends CreatedAtBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "duck_point_history_id")
	private Long id;

	@Column(unique = true, nullable = false, updatable = false)
	private String uuid;

	@Column(name = "left_point", nullable = false)
	private Long leftPoint;

	@Column(name = "change_amount", nullable = false)
	private Long changeAmount;

	@Column(name = "change_status", nullable = false)
	private Boolean changeStatus;

	@Column(name = "history_detail", nullable = false)
	private String historyDetail;
}
