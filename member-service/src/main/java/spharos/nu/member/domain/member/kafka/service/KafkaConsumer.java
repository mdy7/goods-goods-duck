package spharos.nu.member.domain.member.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import spharos.nu.member.domain.member.kafka.dto.FailJointResponseDto;
import spharos.nu.member.domain.member.kafka.dto.JoinRequestDto;
import spharos.nu.member.domain.member.service.MyPageService;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final MyPageService myPageService;
    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics = "join-event", groupId = "join", containerFactory = "kafkaListenerContainerFactory")
    public void joinEvent(JoinRequestDto joinRequestDto){
        log.info("Join Event success");
        try {
            myPageService.saveUserInfo(joinRequestDto);
        }catch (Exception e){
            log.error("Join Event failed: 보상 트랜잭션 발행");
            kafkaProducer.sendFailJoin(new FailJointResponseDto(joinRequestDto.getUuid()));
        }
    }
}
