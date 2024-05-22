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
import spharos.nu.member.global.entity.CreatedAtBaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Table(name = "duck_point_history")
public class DuckPointHistory extends CreatedAtBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "duck_point_history_id")
	private Long id;

	@Column(nullable = false)
	private String uuid;

	@Column(name = "left_point")
	private Long leftPoint;

	@Column(name = "change_amount")
	private Long changeAmount;

	@Column(name = "change_status")
	private boolean changeStatus;

	@Column(name = "history_detail")
	private String historyDetail;
}
