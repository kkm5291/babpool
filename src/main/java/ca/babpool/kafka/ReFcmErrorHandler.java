package ca.babpool.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
@Component
@Slf4j
@RequiredArgsConstructor
public class ReFcmErrorHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        return null;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        String fcmError = "[KafkaErrorHandler] kafkaMessage=[" + message.getPayload() + "], errorMessage=[" + exception.getCause() + "]";
        log.error(fcmError);
        return null;
    }
}
