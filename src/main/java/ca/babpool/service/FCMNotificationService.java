package ca.babpool.service;

import ca.babpool.exception.FcmException;
import ca.babpool.exception.ReFcmException;
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
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
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
    private final RetryTemplate retryTemplate;


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
    public void reSendNotificationByToken(String jsonDto) {
        try {
            retryTemplate.execute(context -> {
                try {
                    processAndSendMessage(jsonDto);
                    return null;
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                } catch (FirebaseMessagingException ex) {
                    throw new FcmException(ex.getMessagingErrorCode());
                }
            });
        } catch (RetryException ex) {
            log.error("재시도 횟수를 초과했습니다.", ex);
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
//                        .setToken("cc3dvL_4PV1xTB1Xdjc-7y:APA91bEZX0XNgGgYVbN8RRS3lhVdobf9Zdp_iJUCeYJtLMORW7HmdHRDbCp9zNkW-WqFr5k-MBe5TjA9Rw76jjaGW-L2WuPVtiQL_ZWqDucb7DhnhBm9iXRC8dkEVuLI5nUjPAxA5lp812")
                        .setNotification(notification)
                        .build();

                firebaseMessaging.send(message);
            } else {
                log.info("로그아웃 된 회원입니다");
            }
        } else {
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
