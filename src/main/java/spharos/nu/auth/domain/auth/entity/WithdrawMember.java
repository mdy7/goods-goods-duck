package spharos.nu.auth.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import spharos.nu.auth.global.entity.CreatedAtBaseEntity;

@Entity
@Getter
@Builder
@ToString
@Table(name = "withdraw_member")
public class WithdrawMember extends CreatedAtBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "withdraw_member_id")
	private Long id;

	@Column(unique = true, nullable = false, updatable = false)
	private String uuid;

	@Column(name = "reason", nullable = false)
	private String reason;
}
