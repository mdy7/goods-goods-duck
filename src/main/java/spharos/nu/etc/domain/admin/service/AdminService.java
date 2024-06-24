package spharos.nu.etc.domain.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.admin.dto.request.NoticeRequestDto;
import spharos.nu.etc.domain.admin.dto.response.NoticeInfo;
import spharos.nu.etc.domain.admin.dto.response.NoticeResponseDto;
import spharos.nu.etc.domain.admin.entity.Notice;
import spharos.nu.etc.domain.admin.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

	private final NoticeRepository noticeRepository;

	public NoticeResponseDto noticeGet(Pageable pageable) {

		Page<Notice> noticePage = noticeRepository.findAll(pageable);

		List<NoticeInfo> noticeList = noticePage.stream()
			.map(notice -> NoticeInfo.builder()
				.id(notice.getId())
				.title(notice.getTitle())
				.content(notice.getContent())
				.createdAt(notice.getCreatedAt())
				.updatedAt(notice.getUpdatedAt())
				.build())
			.toList();

		return NoticeResponseDto.builder()
			.totalCount(noticePage.getTotalElements())
			.nowPage(noticePage.getNumber())
			.maxPage(noticePage.getTotalPages())
			.isLast(noticePage.isLast())
			.noticeList(noticeList)
			.build();
	}

	public Long noticeCreate(NoticeRequestDto noticeRequestDto) {

		Notice notice = noticeRepository.save(Notice.builder()
			.title(noticeRequestDto.getTitle())
			.content(noticeRequestDto.getContent())
			.build());

		return notice.getId();
	}
}
