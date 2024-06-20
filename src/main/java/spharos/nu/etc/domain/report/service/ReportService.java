package spharos.nu.etc.domain.report.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.report.dto.request.ReportRequestDto;
import spharos.nu.etc.domain.report.entity.GoodsReport;
import spharos.nu.etc.domain.report.entity.UserReport;
import spharos.nu.etc.domain.report.repository.GoodsReportRepository;
import spharos.nu.etc.domain.report.repository.UserReportRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

	private final GoodsReportRepository goodsReportRepository;
	private final UserReportRepository userReportRepository;

	/**
	 * 굿즈 신고하기
	 */
	public Void goodsReport(String reporterUuid, String goodsCode, ReportRequestDto reportRequestDto) {

		goodsReportRepository.save(GoodsReport.builder()
			.reporterUuid(reporterUuid)
			.goodsCode(goodsCode)
			.complainStatus(false)
			.complainReason(reportRequestDto.getComplainReason())
			.complainContent(reportRequestDto.getComplainContent())
			.build());

		return null;
	}

	/**
	 * 유저 신고하기
	 */
	public Void userReport(String reporterUuid, String reportedUuid, ReportRequestDto reportRequestDto) {

		userReportRepository.save(UserReport.builder()
			.reporterUuid(reporterUuid)
			.reportedUuid(reportedUuid)
			.complainStatus(false)
			.complainReason(reportRequestDto.getComplainReason())
			.complainContent(reportRequestDto.getComplainContent())
			.build());

		return null;
	}
}
