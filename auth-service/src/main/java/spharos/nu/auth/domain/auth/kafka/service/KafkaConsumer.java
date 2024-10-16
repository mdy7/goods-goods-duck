package spharos.nu.auth.domain.auth.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import spharos.nu.auth.domain.auth.kafka.dto.FailJointResponseDto;
import spharos.nu.auth.domain.auth.service.UserService;


@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final UserService userService;

    @KafkaListener(topics = "fail-join-event", groupId = "fail-join", containerFactory = "kafkaListenerContainerFactory")
    public void joinEvent(FailJointResponseDto failJointResponseDto) {
        log.info("Fail Join Event success");
        userService.failJoin(failJointResponseDto.getUuid());
    }

}
