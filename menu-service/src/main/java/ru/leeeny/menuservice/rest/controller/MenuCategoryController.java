package ru.leeeny.menuservice.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.leeeny.menuservice.api.MenuCategoryApi;
import ru.leeeny.menuservice.dto.CreateMenuCategoryRequest;
import ru.leeeny.menuservice.dto.MenuCategoryPageResponse;
import ru.leeeny.menuservice.dto.MenuCategoryResponse;
import ru.leeeny.menuservice.dto.UpdateMenuCategoryRequest;

import java.util.Optional;

@RestController
public class MenuCategoryController implements MenuCategoryApi {

	@Override
	public ResponseEntity<MenuCategoryResponse> createMenuCategory(CreateMenuCategoryRequest createMenuCategoryRequest) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}

	@Override
	public ResponseEntity<Void> deleteMenuCategory(Long id) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}

	@Override
	public ResponseEntity<MenuCategoryPageResponse> getMenuCategories(Optional<Boolean> active, Optional<Integer> page, Optional<Integer> size) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}

	@Override
	public ResponseEntity<MenuCategoryResponse> getMenuCategoryById(Long id) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}

	@Override
	public ResponseEntity<MenuCategoryResponse> updateMenuCategory(Long id, UpdateMenuCategoryRequest updateMenuCategoryRequest) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}
}
