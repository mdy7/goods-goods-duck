package spharos.nu.etc.domain.report.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import spharos.nu.etc.domain.report.entity.GoodsReport;

@Getter
@Builder
@ToString
public class GoodsReportResponseDto {

	@Schema(description = "총 신고 개수")
	private Long totalCount;

	@Schema(description = "현재 페이지")
	private Integer nowPage;

	@Schema(description = "최대 페이지")
	private Integer maxPage;

	@Schema(description = "마지막 페이지 여부")
	private Boolean isLast;

	@Schema(description = "굿즈 신고 리스트")
	private List<ReportInfo> goodsReportList;
}
