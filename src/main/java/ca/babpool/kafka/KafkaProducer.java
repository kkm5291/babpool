package ca.babpool.kafka;

import ca.babpool.model.dto.firebase.FCMNotificationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    public static final String Topic = "babpool-fcm";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(FCMNotificationRequestDto dto) throws IOException {
        try {
            String jsonDto = objectMapper.writeValueAsString(dto);
            this.kafkaTemplate.send(Topic, jsonDto);
        } catch (Exception e) {
            throw new IOException();
        }
    }
}
