package ca.babpool.service;

import ca.babpool.exception.InvalidApiRequestException;
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
import org.springframework.scheduling.annotation.Async;
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
    @KafkaListener(topics = "babpool-fcm", groupId = "babpool")
    public void sendNotificationByToken(String jsonDto) throws JsonProcessingException {
        FCMNotificationRequestDto requestDto = objectMapper.readValue(jsonDto, FCMNotificationRequestDto.class);
        System.out.println(requestDto.toString());
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
                try {
                    firebaseMessaging.send(message);
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    throw new InvalidApiRequestException();
                }
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
