package spharos.nu.auth.domain.auth.entity;

import java.time.LocalDateTime;

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
import spharos.nu.auth.global.entity.AuditBaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@Table(name = "member")
public class Member extends AuditBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(unique = true, nullable = false, updatable = false)
	private String uuid;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "is_withdraw", nullable = false)
	private boolean isWithdraw;

	@Column(name = "withdraw_at")
	private LocalDateTime withdrawAt;

	public void changePassword(String password) {
		this.password = password;
	}

	public void changeWithdraw(boolean isWithdraw, LocalDateTime withdrawAt) {
		this.isWithdraw = isWithdraw;
		this.withdrawAt = withdrawAt;
	}
}
