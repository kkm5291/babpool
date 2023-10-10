package ca.babpool.kafka;


import ca.babpool.exception.FcmException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FcmErrorHandler implements KafkaListenerErrorHandler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Value("${spring.kafka.r-topic}")
    private String DEAD_TOPIC;

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        return null;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        String fcmError = "[KafkaErrorHandler] kafkaMessage=[" + message.getPayload() + "], errorMessage=[" + exception.getCause() + "]";
        log.error(fcmError);
        FcmException fcmException = (FcmException) exception.getCause();
        MessagingErrorCode status = fcmException.getStatus();

        // status 값을 로그 또는 처리에 활용
        // UNAVAILABLE, INTERNAL시 3회 재전송
        if (status == MessagingErrorCode.UNAVAILABLE || status == MessagingErrorCode.INTERNAL || status == MessagingErrorCode.INVALID_ARGUMENT) {
            kafkaTemplate.send(DEAD_TOPIC, (String) (message.getPayload()));
        } else {
            log.error(fcmError);
        }
        return null;
    }
}
