package ru.leeeny.menuservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.leeeny.menuservice.dto.CreateMenuItemDto;
import ru.leeeny.menuservice.dto.MenuItemDto;
import ru.leeeny.menuservice.dto.OrderMenuRequest;
import ru.leeeny.menuservice.dto.OrderMenuResponse;
import ru.leeeny.menuservice.dto.SortMenu;
import ru.leeeny.menuservice.dto.UpdateMenuItemDto;

import java.util.List;

public interface MenuService {

	MenuItemDto createMenuItem(CreateMenuItemDto dto);

	void deleteMenuItem(Long id);

	MenuItemDto updateMenuItem(Long id, UpdateMenuItemDto dto);

	MenuItemDto getMenuItem(Long id);

	Page<MenuItemDto> getMenuItems(Pageable pageable, boolean isActive);

	List<MenuItemDto> getMenuItemsForCategory(Long categoryId, SortMenu sortMenu, Pageable pageable);

	//TODO: мне было впадлу делать дто уровня сервиса
	OrderMenuResponse getMenusForOrder(OrderMenuRequest orderMenuRequest);
}
