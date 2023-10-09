package ca.babpool.model.dto.firebase;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FCMNotificationRequestDto {
    private String targetUserId;
    private String title;
    private String body;
    private Long restaurantId;

    @Builder
    public FCMNotificationRequestDto(String targetUserId, String title, String body, Long restaurantId) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.body = body;
        this.restaurantId = restaurantId;
    }
}