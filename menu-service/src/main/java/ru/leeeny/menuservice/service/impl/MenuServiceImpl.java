package ru.leeeny.menuservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leeeny.menuservice.dto.CreateMenuItemDto;
import ru.leeeny.menuservice.dto.MenuItemDto;
import ru.leeeny.menuservice.dto.SortByEnum;
import ru.leeeny.menuservice.dto.UpdateMenuItemDto;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.exception.MenuServiceException;
import ru.leeeny.menuservice.mapper.MenuItemMapper;
import ru.leeeny.menuservice.repository.MenuItemRepository;
import ru.leeeny.menuservice.service.MenuService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

	private final MenuItemRepository menuItemRepository;
	private final MenuItemMapper menuItemMapper;

	@Override
	public MenuItemDto createMenuItem(CreateMenuItemDto dto) {
		MenuItem item = menuItemRepository.save(menuItemMapper.toEntity(dto));
		log.info("Menu created item={}", item);
		return menuItemMapper.toDto(item);
	}

	@Override
	public void deleteMenuItem(Long id) {
		if (!menuItemRepository.existsById(id)) {
			throw new MenuServiceException("Menu Item with id=%d not found".formatted(id), HttpStatus.NOT_FOUND);
		}
		menuItemRepository.deleteById(id);
		log.info("Menu deleted item={}", id);
	}

	@Override
	@Transactional
	public MenuItemDto updateMenuItem(Long id, UpdateMenuItemDto dto) {
		int affectedRows = menuItemRepository.updateMenuItem(id, dto);
		if (affectedRows == 0) {
			throw new MenuServiceException(
					"Menu item with id=%d is equal to the existing data, there's nothing to update"
							.formatted(id),
					HttpStatus.CONFLICT);
		}
		log.info("Menu updated item={}", id);
		return getMenuItem(id);
	}

	@Override
	public MenuItemDto getMenuItem(Long id) {
		return menuItemRepository.findById(id).map(menuItemMapper::toDto).orElseThrow(
				() -> new MenuServiceException("Menu Item with id=%d not found".formatted(id), HttpStatus.NOT_FOUND)
		);
	}

	@Override
	public List<MenuItemDto> getMenuItems() {
		return menuItemMapper.toDtos(menuItemRepository.findAll());
	}

	@Override
	public List<MenuItemDto> getMenuItemsForCategory(Long categoryId, SortByEnum sortByEnum) {
		return menuItemMapper
				.toDtos(menuItemRepository.getMenusFor(categoryId, sortByEnum));
	}
}
