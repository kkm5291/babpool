package ca.babpool.model.dto.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMenuDto {
    private Long menuId;
    private Long restaurantId;
}
