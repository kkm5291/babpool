package ca.babpool.service;

import ca.babpool.model.dto.menu.DeleteMenuDto;
import ca.babpool.model.dto.menu.MenuDto;
import ca.babpool.model.dto.menu.RepresentativeMenuDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MenuService {
    List<MenuDto> selectMenuByRestaurantId(Long restaurantId);
    int updateMenu(MenuDto dto);

    int insertMenu(MenuDto dto) throws IOException;

    List<MenuDto> selectRepresentativeMenu(Long restaurantId);

    int updateRepresentativeMenu(RepresentativeMenuDto dto);

    int deleteMenuByMenuId(DeleteMenuDto dto);

    List<MenuDto> selectAllMenuByRestaurantId(Long restaurantId);
    List<MenuDto> selectHideOrSoldOutMenu(Long restaurantId);

}
