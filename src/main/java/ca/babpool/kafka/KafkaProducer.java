package ca.babpool.kafka;

import ca.babpool.model.dto.firebase.FCMNotificationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    @Value("${spring.kafka.topic}")
    private String TOPIC;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(FCMNotificationRequestDto dto) throws IOException {
        try {
            String jsonDto = objectMapper.writeValueAsString(dto);
            this.kafkaTemplate.send(TOPIC, jsonDto);
        } catch (Exception e) {
            throw new IOException();
        }
    }
}
