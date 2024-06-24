package spharos.nu.etc.domain.report.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.report.dto.request.ReportRequestDto;
import spharos.nu.etc.domain.report.dto.response.GoodsReportResponseDto;
import spharos.nu.etc.domain.report.dto.response.ReportInfo;
import spharos.nu.etc.domain.report.dto.response.UserReportResponseDto;
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

	/**
	 * 굿즈 신고 조회하기
	 */
	public GoodsReportResponseDto goodsReportGet(Pageable pageable) {

		Page<GoodsReport> goodsReportPage = goodsReportRepository.findAll(pageable);

		List<ReportInfo> goodsReportList = goodsReportPage.stream()
			.map(goods -> ReportInfo.builder()
				.reporterUuid(goods.getReporterUuid())
				.subject(goods.getGoodsCode())
				.complainStatus(goods.isComplainStatus())
				.complainReason(goods.getComplainReason())
				.complainContent(goods.getComplainContent())
				.createdAt(goods.getCreatedAt())
				.build())
			.toList();

		return GoodsReportResponseDto.builder()
			.totalCount(goodsReportPage.getTotalElements())
			.nowPage(goodsReportPage.getNumber())
			.maxPage(goodsReportPage.getTotalPages())
			.isLast(goodsReportPage.isLast())
			.goodsReportList(goodsReportList)
			.build();
	}

	/**
	 * 회원 신고 조회하기
	 */
	public UserReportResponseDto userReportGet(Pageable pageable) {

		Page<UserReport> userReportPage = userReportRepository.findAll(pageable);

		List<ReportInfo> userReportList = userReportPage.stream()
			.map(user -> ReportInfo.builder()
				.reporterUuid(user.getReporterUuid())
				.subject(user.getReportedUuid())
				.complainStatus(user.isComplainStatus())
				.complainReason(user.getComplainReason())
				.complainContent(user.getComplainContent())
				.createdAt(user.getCreatedAt())
				.build())
			.toList();

		return UserReportResponseDto.builder()
			.totalCount(userReportPage.getTotalElements())
			.nowPage(userReportPage.getNumber())
			.maxPage(userReportPage.getTotalPages())
			.isLast(userReportPage.isLast())
			.userReportList(userReportList)
			.build();
	}
}
