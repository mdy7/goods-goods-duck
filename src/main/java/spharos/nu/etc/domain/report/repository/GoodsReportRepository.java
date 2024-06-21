package spharos.nu.etc.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spharos.nu.etc.domain.report.entity.GoodsReport;

public interface GoodsReportRepository extends JpaRepository<GoodsReport, Long> {
}
