package spharos.nu.etc.domain.report.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.report.dto.request.GoodsReportRequestDto;
import spharos.nu.etc.domain.report.entity.GoodsReport;
import spharos.nu.etc.domain.report.repository.GoodsReportRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

	private final GoodsReportRepository goodsReportRepository;

	public Void goodsReport(String reporterUuid, String goodsCode, GoodsReportRequestDto goodsReportRequestDto) {

		goodsReportRepository.save(GoodsReport.builder()
			.reporterUuid(reporterUuid)
			.goodsCode(goodsCode)
			.complainStatus(false)
			.complainReason(goodsReportRequestDto.getComplainReason())
			.complainContent(goodsReportRequestDto.getComplainContent())
			.build());

		return null;
	}
}
