package ru.leeeny.menuservice.service;

import ru.leeeny.menuservice.dto.CreateMenuItemDto;
import ru.leeeny.menuservice.dto.MenuItemDto;
import ru.leeeny.menuservice.dto.SortByEnum;
import ru.leeeny.menuservice.dto.UpdateMenuItemDto;

import java.util.List;

public interface MenuService {

	MenuItemDto createMenuItem(CreateMenuItemDto dto);

	void deleteMenuItem(Long id);

	MenuItemDto updateMenuItem(Long id, UpdateMenuItemDto dto);

	MenuItemDto getMenuItem(Long id);

	List<MenuItemDto> getMenuItems();

	List<MenuItemDto> getMenuItemsForCategory(Long categoryId, SortByEnum sortByEnum);
}
