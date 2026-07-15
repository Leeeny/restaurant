package ru.leeeny.menuservice.service;

import ru.leeeny.menuservice.dto.IngredientDto;

import java.util.List;

public interface MenuIngredientService {

	IngredientDto addIngredient(Long menuId, Long ingredientId, Double grams);

	List<IngredientDto> getIngredients(Long menuId);

	IngredientDto updateIngredientInMenu(Long menuId, Long ingredientId, Double grams);

	void deleteIngredient(Long menuId, Long ingredientId);
}
