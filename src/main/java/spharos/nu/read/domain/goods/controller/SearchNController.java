package spharos.nu.read.domain.goods.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.read.domain.goods.dto.response.SearchResultResponseDto;
import spharos.nu.read.domain.goods.dto.response.SearchWordDto;
import spharos.nu.read.domain.goods.service.SearchService;
import spharos.nu.read.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/read-n")
@Slf4j
@Tag(name = "SearchN", description = "토큰 검증이 필요 없는 읽기용 굿즈 검색 컨트롤러")
public class SearchNController {

	private final SearchService searchService;

	@GetMapping("/search")
	@Operation(summary = "검색 결과", description = "키워드 검색 결과를 페이지네이션, 정렬, 필터링 가능")
	public ResponseEntity<ApiResponse<SearchResultResponseDto>> getSearchResult(@RequestParam("keyword") String keyword,
		@RequestParam(value = "sort", defaultValue = "createdAt") String sort,
		@RequestParam(value = "isTradingOnly", defaultValue = "false") boolean isTradingOnly,
		@PageableDefault(size = 10, page = 0) Pageable pageable) {

		return ApiResponse.success(searchService.searchResultGet(keyword, sort, isTradingOnly, pageable), "검색 성공");
	}

	@GetMapping("/search-list")
	@Operation(summary = "검색어 리스트", description = "키워드를 칠때마다 보여줄 검색어 리스트")
	public ResponseEntity<ApiResponse<List<SearchWordDto>>> getSearchTextList(@RequestParam("keyword") String keyword) {

		return ApiResponse.success(searchService.searchWordListGet(keyword), "검색어 리스트 조회 성공");
	}
}