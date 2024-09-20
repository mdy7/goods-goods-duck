package spharos.nu.etc.domain.report.entity;

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
import spharos.nu.etc.global.entity.AuditBaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "user_report")
public class UserReport extends AuditBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_report_id", nullable = false)
	private Long id;

	@Column(name = "reporter_uuid", nullable = false)
	private String reporterUuid;

	@Column(name = "reported_uuid", nullable = false)
	private String reportedUuid;

	@Column(name = "complain_status", nullable = false)
	private boolean complainStatus;

	@Column(name = "complain_reason", nullable = false)
	private String complainReason;

	@Column(name = "complain_content", nullable = false)
	private String complainContent;
}
