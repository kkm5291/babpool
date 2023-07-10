package ca.babpool.model.dto.menu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RepresentativeMenuDto {
    private Long restaurantId;
    private List<Long> checkedMenuIds;
}
