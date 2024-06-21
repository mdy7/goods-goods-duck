package spharos.nu.member.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spharos.nu.member.domain.member.entity.DuckPoint;
import spharos.nu.member.domain.member.entity.DuckPointHistory;
import spharos.nu.member.domain.member.repository.DuckPointHistoryRepository;
import spharos.nu.member.domain.member.repository.DuckPointRepository;
import spharos.nu.member.domain.payment.dto.ApproveResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DuckPointService {

    private final DuckPointRepository duckPointRepository;
    private final DuckPointHistoryRepository duckPointHistoryRepository;

    /**
     * 포인트 충전
     */
    @Transactional
    public void updatePoint(ApproveResponseDto approveResponseDto) {
        DuckPoint duckPoint = duckPointRepository.findByUuid(approveResponseDto.getPartner_user_id()).orElseGet(
                () -> duckPointRepository.save(DuckPoint.builder()
                        .uuid(approveResponseDto.getPartner_user_id())
                        .nowPoint(0L)
                        .build())
        );

        DuckPoint savedDuckPoint = duckPointRepository.save(DuckPoint.builder()
                .id(duckPoint.getId())
                .uuid(duckPoint.getUuid())
                .nowPoint(duckPoint.getNowPoint() + approveResponseDto.getAmount().getTotal())
                .build());

        duckPointHistoryRepository.save(DuckPointHistory.builder()
                .uuid(duckPoint.getUuid())
                .leftPoint(savedDuckPoint.getNowPoint())
                .changeAmount(Long.valueOf(approveResponseDto.getAmount().getTotal()))
                .changeStatus(true)
                .historyDetail(approveResponseDto.getItem_name())
                .build());

        log.info("{} 회원 포인트 충전 완료: {}", duckPoint.getUuid(), savedDuckPoint.getNowPoint());
    }




}
