package spharos.nu.member.domain.member.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spharos.nu.member.domain.member.kafka.dto.FailJointResponseDto;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, FailJointResponseDto> kafkaTemplate;

    public void sendFailJoin(FailJointResponseDto failJointResponseDto) {
        kafkaTemplate.send("fail-join-event", failJointResponseDto);
    }
}
