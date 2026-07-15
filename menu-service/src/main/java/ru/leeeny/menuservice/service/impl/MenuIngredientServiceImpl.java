package ru.leeeny.menuservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leeeny.menuservice.dto.IngredientDto;
import ru.leeeny.menuservice.entity.Ingredient;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.entity.MenuItemIngredient;
import ru.leeeny.menuservice.entity.MenuItemIngredientId;
import ru.leeeny.menuservice.exception.MenuServiceException;
import ru.leeeny.menuservice.mapper.IngredientMapper;
import ru.leeeny.menuservice.repository.IngredientRepository;
import ru.leeeny.menuservice.repository.MenuIngredientRepository;
import ru.leeeny.menuservice.repository.MenuItemRepository;
import ru.leeeny.menuservice.service.MenuIngredientService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuIngredientServiceImpl implements MenuIngredientService {

	private final MenuIngredientRepository menuIngredientRepository;
	private final MenuItemRepository menuItemRepository;
	private final IngredientRepository ingredientRepository;

	private final IngredientMapper ingredientMapper;


	@Override
	public IngredientDto addIngredient(Long menuId, Long ingredientId, Double grams) {
		if (grams == null || grams <= 0) {
			throw new MenuServiceException("Weight is must be greater than 0", HttpStatus.BAD_REQUEST);
		}
		MenuItem item = menuItemRepository.findById(menuId)
				.orElseThrow(() ->
						new MenuServiceException(
								"Menu Item with id=%d not found".formatted(menuId),
								HttpStatus.NOT_FOUND
						));
		Ingredient ingredient = ingredientRepository.findById(ingredientId)
				.orElseThrow(() ->
						new MenuServiceException(
								"Ingredient with id=%d not found".formatted(ingredientId),
								HttpStatus.NOT_FOUND
						));
		MenuItemIngredientId id = new MenuItemIngredientId(menuId, ingredientId);
		if (menuIngredientRepository.existsById(id)) {
			throw new MenuServiceException(
					"Ingredient with id=%d is already added to menu item=%d".formatted(ingredientId, menuId),
					HttpStatus.CONFLICT);
		}

		MenuItemIngredient relation = MenuItemIngredient.builder()
				.menuItem(item)
				.ingredient(ingredient)
				.weightGrams(BigDecimal.valueOf(grams))
				.build();
		menuIngredientRepository.save(relation);
		log.info("Menu ingredient added={}", relation);
		return ingredientMapper.toDto(ingredient);
	}

	@Override
	public List<IngredientDto> getIngredients(Long menuId) {
		return ingredientMapper.toDtos(ingredientRepository.getIngredientsByMenuId(menuId));
	}

	@Override
	@Transactional
	public IngredientDto updateIngredientInMenu(Long menuId, Long ingredientId, Double grams) {
		if (grams == null || grams <= 0) {
			throw new MenuServiceException("Weight is must be greater than 0", HttpStatus.BAD_REQUEST);
		}
		MenuItemIngredientId id = new MenuItemIngredientId(menuId, ingredientId);
		MenuItemIngredient menuItemIngredient = menuIngredientRepository.findById(id)
				.orElseThrow(() ->
						new MenuServiceException(
								"There's not ingredient with id=%d in menu item=%d".formatted(ingredientId, menuId),
								HttpStatus.NOT_FOUND
						));
		menuItemIngredient.setWeightGrams(BigDecimal.valueOf(grams));
		log.info("Menu ingredient updated={}", menuItemIngredient);
		return ingredientMapper.toDto(ingredientRepository.findById(ingredientId).orElseThrow(() ->
				new MenuServiceException(
						"Ingredient with id=%d not found".formatted(ingredientId),
						HttpStatus.NOT_FOUND
				)));
	}

	@Override
	public void deleteIngredient(Long menuId, Long ingredientId) {
		MenuItemIngredientId id = new MenuItemIngredientId(menuId, ingredientId);
		if (!menuIngredientRepository.existsById(id))
			throw new MenuServiceException(
					"There's not ingredient with id=%d in menu item=%d".formatted(ingredientId, menuId),
					HttpStatus.NOT_FOUND);
		menuIngredientRepository.deleteById(id);
	}
}
