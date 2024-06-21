package spharos.nu.auth.domain.auth.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spharos.nu.auth.domain.auth.dto.response.WithdrawMemberResponseDto;
import spharos.nu.auth.domain.auth.entity.WithdrawMember;
import spharos.nu.auth.domain.auth.repository.WithdrawRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

	private final WithdrawRepository withdrawRepository;

	/**
	 * 탈퇴 회원 조회
	 */
	public WithdrawMemberResponseDto withdrawMemberGet(Pageable pageable) {

		Page<WithdrawMember> withdrawMemberPage = withdrawRepository.findAll(pageable);
		List<WithdrawMember> memberList = withdrawMemberPage.getContent();

		return WithdrawMemberResponseDto.builder()
			.totalCount(withdrawMemberPage.getTotalElements())
			.nowPage(withdrawMemberPage.getNumber())
			.maxPage(withdrawMemberPage.getTotalPages())
			.isLast(withdrawMemberPage.isLast())
			.memberList(memberList)
			.build();
	}
}
