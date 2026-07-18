package ru.leeeny.menuservice.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.leeeny.menuservice.api.IngredientApi;
import ru.leeeny.menuservice.dto.CreateIngredientRequest;
import ru.leeeny.menuservice.dto.IngredientPageResponse;
import ru.leeeny.menuservice.dto.IngredientResponse;
import ru.leeeny.menuservice.dto.UpdateIngredientRequest;

import java.util.Optional;

@RestController
public class IngredientController implements IngredientApi {

	@Override
	public ResponseEntity<IngredientResponse> createIngredient(CreateIngredientRequest createIngredientRequest) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}

	@Override
	public ResponseEntity<Void> deleteIngredient(Long id) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}

	@Override
	public ResponseEntity<IngredientResponse> getIngredientById(Long id) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}

	@Override
	public ResponseEntity<IngredientPageResponse> getIngredients(Optional<String> name, Optional<Integer> page, Optional<Integer> size) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}

	@Override
	public ResponseEntity<IngredientResponse> updateIngredient(Long id, UpdateIngredientRequest updateIngredientRequest) {
		throw new ResponseStatusException(
				HttpStatus.NOT_IMPLEMENTED,
				"This endpoint is not implemented yet");
	}
}
