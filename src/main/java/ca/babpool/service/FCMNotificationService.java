package ca.babpool.service;

import ca.babpool.exception.FcmException;
import ca.babpool.mapper.MemberMapper;
import ca.babpool.mapper.RestaurantMapper;
import ca.babpool.model.dto.firebase.FCMNotificationRequestDto;
import ca.babpool.model.dto.restaurant.RestaurantFcmDto;
import ca.babpool.model.dto.restaurant.RestaurantNewOrderDto;
import ca.babpool.model.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberMapper memberMapper;
    private final RestaurantMapper restaurantMapper;
    private final ObjectMapper objectMapper;


    @Transactional(propagation = Propagation.NESTED)
    @KafkaListener(topics = "babpool-fcm", groupId = "babpool", errorHandler = "fcmErrorHandler")
    public void sendNotificationByToken(String jsonDto) throws JsonProcessingException {
        try {
            processAndSendMessage(jsonDto);
        } catch (FirebaseMessagingException e) {
            throw new FcmException(e.getMessagingErrorCode());
        }
    }

    @KafkaListener(topics = "babpool-fcm-r", groupId = "babpool", errorHandler = "reFcmErrorHandler")
    public void reSendNotificationByToken(String jsonDto) throws JsonProcessingException {

        int maxRetryAttempts = 2;
        int currentRetryAttempt = 0;

        while (currentRetryAttempt < maxRetryAttempts) {
            try {
                processAndSendMessage(jsonDto);
                break;
            } catch (FirebaseMessagingException e) {
                currentRetryAttempt++;
                if (currentRetryAttempt < maxRetryAttempts) {
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new FcmException(e.getMessagingErrorCode());
                }
            }
        }
    }

    private void processAndSendMessage(String jsonDto) throws JsonProcessingException, FirebaseMessagingException {
        FCMNotificationRequestDto requestDto = objectMapper.readValue(jsonDto, FCMNotificationRequestDto.class);
        Optional<Member> member = Optional.ofNullable(memberMapper.findById(requestDto.getTargetUserId()));
        if (member.isPresent()) {
            if (member.get().getMemberFireBaseToken() != null) {
                Notification notification = Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getBody())
                        .build();

                Message message = Message.builder()
                        .putData("restaurantId", String.valueOf(requestDto.getRestaurantId()))
                        .setToken(member.get().getMemberFireBaseToken())
                        .setToken("cc3dvL_4PV1xTB1Xdjc-7y:APA91bEZX0XNgGgYVbN8RRS3lhVdobf9Zdp_iJUCeYJtLMORW7HmdHRDbCp9zNkW-WqFr5k-MBe5TjA9Rw76jjaGW-L2WuPVtiQL_ZWqDucb7DhnhBm9iXRC8dkEVuLI5nUjPAxA5lp812")
                        .setNotification(notification)
                        .build();

                firebaseMessaging.send(message);
            } else {
                // 여기도 예외 처리
                log.info("로그아웃 된 회원입니다");
            }
        } else {
            // 여기도 예외 처리
            log.info("잘못된 사용자 입니다.");
        }
    }

    public FCMNotificationRequestDto createNotificationDto(RestaurantNewOrderDto restaurantNewOrderDto) {
        Long restaurantId = restaurantNewOrderDto.getOrderDetailsDto().getRestaurantId();
        RestaurantFcmDto restaurantFcmDto = restaurantMapper.findMemberIdAndRestaurantNameByRestaurantId(restaurantId);

        return FCMNotificationRequestDto.builder()
                .targetUserId(restaurantFcmDto.getMemberId())
                .body(restaurantFcmDto.getRestaurantName() + "에 주문이 들어왔어요")
                .title("BabPool 신규주문")
                .restaurantId(restaurantNewOrderDto.getOrderDetailsDto().getRestaurantId())
                .build();
    }
}
