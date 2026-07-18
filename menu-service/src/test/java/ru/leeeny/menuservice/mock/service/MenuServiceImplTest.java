package ru.leeeny.menuservice.mock.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.leeeny.menuservice.dto.CreateMenuItemDto;
import ru.leeeny.menuservice.dto.MenuItemDto;
import ru.leeeny.menuservice.dto.SortMenu;
import ru.leeeny.menuservice.dto.UpdateMenuItemDto;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.exception.MenuServiceException;
import ru.leeeny.menuservice.mapper.MenuItemMapper;
import ru.leeeny.menuservice.repository.MenuItemRepository;
import ru.leeeny.menuservice.service.impl.MenuServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

	@Mock
	private MenuItemRepository menuItemRepository;

	@Mock
	private MenuItemMapper menuItemMapper;

	@InjectMocks
	private MenuServiceImpl menuService;

	private MenuItem menuItem;
	private MenuItemDto menuItemDto;

	@BeforeEach
	void setUp() {
		menuItem = mock(MenuItem.class);
		menuItemDto = mock(MenuItemDto.class);
	}

	@Test
	void createMenuItem_shouldSaveEntityAndReturnDto() {
		CreateMenuItemDto createDto = mock(CreateMenuItemDto.class);
		when(menuItemMapper.toEntity(createDto)).thenReturn(menuItem);
		when(menuItemRepository.save(menuItem)).thenReturn(menuItem);
		when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemDto);

		MenuItemDto result = menuService.createMenuItem(createDto);

		assertThat(result).isEqualTo(menuItemDto);
		verify(menuItemRepository).save(menuItem);
	}

	@Test
	void deleteMenuItem_whenItemExists_deletesIt() {
		when(menuItemRepository.existsById(1L)).thenReturn(true);

		menuService.deleteMenuItem(1L);

		verify(menuItemRepository).deleteById(1L);
	}

	@Test
	void deleteMenuItem_whenItemDoesNotExist_throwsNotFoundAndDoesNotCallDelete() {
		when(menuItemRepository.existsById(999L)).thenReturn(false);

		assertThatThrownBy(() -> menuService.deleteMenuItem(999L))
				.isInstanceOf(MenuServiceException.class)
				.hasMessageContaining("999");

		verify(menuItemRepository, never()).deleteById(999L);
	}

	@Test
	void updateMenuItem_whenRowsAffected_returnsUpdatedDto() {
		UpdateMenuItemDto updateDto = mock(UpdateMenuItemDto.class);
		when(menuItemRepository.updateMenuItem(1L, updateDto)).thenReturn(1);
		when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
		when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemDto);

		MenuItemDto result = menuService.updateMenuItem(1L, updateDto);

		assertThat(result).isEqualTo(menuItemDto);
	}

	@Test
	void updateMenuItem_whenNoRowsAffected_throwsConflict() {
		UpdateMenuItemDto updateDto = mock(UpdateMenuItemDto.class);
		when(menuItemRepository.updateMenuItem(1L, updateDto)).thenReturn(0);

		assertThatThrownBy(() -> menuService.updateMenuItem(1L, updateDto))
				.isInstanceOf(MenuServiceException.class)
				.hasMessageContaining("1");
	}

	@Test
	void getMenuItem_whenFound_returnsDto() {
		when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
		when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemDto);

		MenuItemDto result = menuService.getMenuItem(1L);

		assertThat(result).isEqualTo(menuItemDto);
	}

	@Test
	void getMenuItem_whenNotFound_throwsMenuServiceExceptionWith404() {
		when(menuItemRepository.findById(42L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> menuService.getMenuItem(42L))
				.isInstanceOf(MenuServiceException.class)
				.hasMessageContaining("42");
	}

	@Test
	void getMenuItems_returnsMappedPage() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<MenuItem> page = new PageImpl<>(List.of(menuItem), pageable, 1);

		when(menuItemRepository.findAll(pageable)).thenReturn(page);
		when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemDto);

		Page<MenuItemDto> result = menuService.getMenuItems(pageable, true);

		assertThat(result.getContent()).containsExactly(menuItemDto);
		assertThat(result.getTotalElements()).isEqualTo(1);

		verify(menuItemRepository).findAll(pageable);
		verify(menuItemMapper).toDto(menuItem);
	}

	@Test
	void getMenuItemsForCategory_returnsMappedListForGivenSort() {
		SortMenu sortBy = SortMenu.PRICE_ASC;
		Pageable pageable = PageRequest.of(0, 10);
		List<MenuItem> entities = List.of(menuItem);
		List<MenuItemDto> dtos = List.of(menuItemDto);
		when(menuItemRepository.getMenusFor(5L, sortBy, pageable)).thenReturn(entities);
		when(menuItemMapper.toDtos(entities)).thenReturn(dtos);

		List<MenuItemDto> result = menuService.getMenuItemsForCategory(5L, sortBy, pageable);

		assertThat(result).isEqualTo(dtos);
		verify(menuItemRepository).getMenusFor(5L, sortBy, pageable);
	}
}