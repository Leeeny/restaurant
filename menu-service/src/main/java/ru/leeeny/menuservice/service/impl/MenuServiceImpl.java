package ru.leeeny.menuservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leeeny.menuservice.dto.CreateMenuItemDto;
import ru.leeeny.menuservice.dto.MenuItemDto;
import ru.leeeny.menuservice.dto.SortMenu;
import ru.leeeny.menuservice.dto.UpdateMenuItemDto;
import ru.leeeny.menuservice.entity.MenuCategory;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.exception.MenuServiceException;
import ru.leeeny.menuservice.mapper.MenuItemMapper;
import ru.leeeny.menuservice.repository.CategoryRepository;
import ru.leeeny.menuservice.repository.MenuItemRepository;
import ru.leeeny.menuservice.service.MenuService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

	private final MenuItemRepository menuItemRepository;
	private final CategoryRepository categoryRepository;
	private final MenuItemMapper menuItemMapper;

	@Override
	public MenuItemDto createMenuItem(CreateMenuItemDto dto) {
		MenuItem menuItem = menuItemMapper.toEntity(dto);

		MenuCategory category = categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() ->
						new MenuServiceException(
								"Category with id=%d for menu item not found".formatted(dto.getCategoryId()),
								HttpStatus.NOT_FOUND)
				);
		menuItem.setMenuCategory(category);

		return menuItemMapper.toDto(
				menuItemRepository.save(menuItem)
		);
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
			if (!menuItemRepository.existsById(id)) {
				throw new MenuServiceException("Menu Item with id=%d not found".formatted(id), HttpStatus.NOT_FOUND);
			}
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
	public Page<MenuItemDto> getMenuItems(Pageable pageable, boolean active) { //TODO
		return menuItemRepository
				.findAll(pageable)
				.map(menuItemMapper::toDto);
	}

	@Override
	public List<MenuItemDto> getMenuItemsForCategory(Long categoryId, SortMenu sortMenu, Pageable pageable) {
		return menuItemMapper
				.toDtos(menuItemRepository.getMenusFor(categoryId, sortMenu, pageable));
	}
}
