package ru.leeeny.menuservice.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.leeeny.menuservice.api.MenuItemApi;
import ru.leeeny.menuservice.dto.CreateMenuItemIngredientRequest;
import ru.leeeny.menuservice.dto.CreateMenuItemRequest;
import ru.leeeny.menuservice.dto.IngredientDto;
import ru.leeeny.menuservice.dto.IngredientSortBy;
import ru.leeeny.menuservice.dto.MenuItemDto;
import ru.leeeny.menuservice.dto.MenuItemIngredientPageResponse;
import ru.leeeny.menuservice.dto.MenuItemIngredientResponse;
import ru.leeeny.menuservice.dto.MenuItemPageResponse;
import ru.leeeny.menuservice.dto.MenuItemResponse;
import ru.leeeny.menuservice.dto.OrderMenuRequest;
import ru.leeeny.menuservice.dto.OrderMenuResponse;
import ru.leeeny.menuservice.dto.SortBy;
import ru.leeeny.menuservice.dto.SortMenu;
import ru.leeeny.menuservice.dto.UpdateMenuItemIngredientRequest;
import ru.leeeny.menuservice.dto.UpdateMenuRequest;
import ru.leeeny.menuservice.mapper.RestIngredientMapper;
import ru.leeeny.menuservice.mapper.RestMenuItemIngredientMapper;
import ru.leeeny.menuservice.mapper.RestMenuItemMapper;
import ru.leeeny.menuservice.service.MenuIngredientService;
import ru.leeeny.menuservice.service.MenuService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MenuItemController implements MenuItemApi {

	private final MenuService menuService;
	private final MenuIngredientService menuIngredientService;

	private final RestMenuItemMapper menuItemMapper;
	private final RestMenuItemIngredientMapper menuItemIngredientMapper;
	private final RestIngredientMapper ingredientMapper;


	@Override
	public ResponseEntity<MenuItemIngredientResponse> addIngredientToMenuItem(
			Long menuItemId,
			CreateMenuItemIngredientRequest dto) {
		IngredientDto response = menuIngredientService
				.addIngredient(menuItemId, dto.getIngredientId(), dto.getWeightGrams());

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(menuItemIngredientMapper.toRestDto(response));
	}

	@Override
	public ResponseEntity<MenuItemResponse> createMenuItem(CreateMenuItemRequest createMenuItemRequest) {
		MenuItemResponse responseDto = menuItemMapper.toRestDto(
				menuService.createMenuItem(menuItemMapper.toServiceDto(createMenuItemRequest))
		);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(responseDto);
	}

	@Override
	public ResponseEntity<Void> deleteMenuItem(Long id) {
		menuService.deleteMenuItem(id);
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
	}

	@Override
	public ResponseEntity<MenuItemResponse> getMenuItemById(Long id) {
		MenuItemResponse responseDto = menuItemMapper.toRestDto(menuService.getMenuItem(id));
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(responseDto);
	}

	@Override
	public ResponseEntity<MenuItemIngredientPageResponse> getMenuItemIngredients(
			Long menuItemId,
			Optional<Integer> page,
			Optional<Integer> size,
			Optional<IngredientSortBy> sortBy) {

		int pageNumber = page.orElse(0);
		int pageSize = size.orElse(10);
		IngredientSortBy sort = sortBy.orElse(IngredientSortBy.AZ);

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		List<MenuItemIngredientResponse> response = ingredientMapper.toRestDtos(
				menuIngredientService.getIngredients(menuItemId, sort, pageable)
		);

		MenuItemIngredientPageResponse pageResponse = new MenuItemIngredientPageResponse();
		pageResponse.setPage(pageNumber);
		pageResponse.setSize(pageSize);
		pageResponse.setContent(response);

		return ResponseEntity.ok(pageResponse);
	}

	@Override
	public ResponseEntity<MenuItemPageResponse> getMenuItems(
			Optional<Long> categoryId,
			Optional<Boolean> active,
			Optional<Integer> page,
			Optional<Integer> size) {

		int pageNumber = page.orElse(0);
		int pageSize = size.orElse(10);
		Boolean isActive = active.orElse(true);

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		MenuItemPageResponse pageResponse = menuItemMapper.toPageResponse(
				menuService.getMenuItems(pageable, isActive)
		);

		return ResponseEntity.ok(pageResponse);
	}

	@Override
	public ResponseEntity<MenuItemPageResponse> getMenuItemsByCategory(
			Long categoryId,
			Optional<SortBy> sortBy,
			Optional<Integer> page,
			Optional<Integer> size) {

		int pageNumber = page.orElse(0);
		int pageSize = size.orElse(10);

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		SortMenu sort = sortBy
				.map(Enum::toString)
				.map(SortMenu::fromString)
				.orElse(SortMenu.AZ);

		List<MenuItemResponse> response = menuItemMapper.toRestDtos(
				menuService.getMenuItemsForCategory(categoryId, sort, pageable)
		);

		MenuItemPageResponse pageResponse = new MenuItemPageResponse();
		pageResponse.setPage(pageNumber);
		pageResponse.setSize(pageSize);
		pageResponse.setContent(response);

		return ResponseEntity.ok(pageResponse);
	}

	@Override
	public ResponseEntity<Void> removeIngredientFromMenuItem(Long menuItemId, Long ingredientId) {
		menuIngredientService.deleteIngredient(menuItemId, ingredientId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Override
	public ResponseEntity<MenuItemResponse> updateMenuItem(Long id, UpdateMenuRequest updateMenuRequest) {
		MenuItemDto itemDto = menuService.updateMenuItem(id, menuItemMapper.toServiceDto(updateMenuRequest));

		return ResponseEntity.status(HttpStatus.OK)
				.body(menuItemMapper.toRestDto(itemDto));
	}

	@Override
	public ResponseEntity<MenuItemIngredientResponse> updateMenuItemIngredient(
			Long menuItemId,
			Long ingredientId,
			UpdateMenuItemIngredientRequest updateMenuItemIngredientRequest) {

		IngredientDto responseDto = menuIngredientService
				.updateIngredientInMenu(menuItemId, ingredientId, updateMenuItemIngredientRequest.getWeightGrams());

		return ResponseEntity.status(HttpStatus.OK)
				.body(menuItemIngredientMapper.toRestDto(responseDto));
	}

	@Override
	public ResponseEntity<OrderMenuResponse> getMenusForOrder(OrderMenuRequest orderMenuRequest) {
		log.info("Received request to GET menus with names: {}", orderMenuRequest.getMenuNames());
		return ResponseEntity
				.ok(menuService.getMenusForOrder(orderMenuRequest));
	}
}
