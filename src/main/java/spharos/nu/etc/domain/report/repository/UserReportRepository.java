package spharos.nu.etc.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spharos.nu.etc.domain.report.entity.UserReport;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
}
