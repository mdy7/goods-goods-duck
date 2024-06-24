package spharos.nu.etc.domain.notice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.etc.domain.notice.dto.request.NoticeRequestDto;
import spharos.nu.etc.domain.notice.dto.response.NoticeInfo;
import spharos.nu.etc.domain.notice.dto.response.NoticeResponseDto;
import spharos.nu.etc.domain.notice.entity.Notice;
import spharos.nu.etc.domain.notice.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

	private final NoticeRepository noticeRepository;

	public NoticeResponseDto noticesGet(Pageable pageable) {

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

	public NoticeInfo noticeGet(Long noticeId) {

		Notice notice = noticeRepository.findById(noticeId).orElseThrow();

		return NoticeInfo.builder()
			.id(noticeId)
			.title(notice.getTitle())
			.content(notice.getContent())
			.createdAt(notice.getCreatedAt())
			.updatedAt(notice.getUpdatedAt())
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
