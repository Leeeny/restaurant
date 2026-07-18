package ru.leeeny.menuservice.service;

import org.springframework.data.domain.Pageable;
import ru.leeeny.menuservice.dto.IngredientDto;
import ru.leeeny.menuservice.dto.IngredientSortBy;

import java.util.List;

public interface MenuIngredientService {

	IngredientDto addIngredient(Long menuId, Long ingredientId, Double grams);

	List<IngredientDto> getIngredients(Long menuId, IngredientSortBy sortBy, Pageable pageable);

	IngredientDto updateIngredientInMenu(Long menuId, Long ingredientId, Double grams);

	void deleteIngredient(Long menuId, Long ingredientId);
}
