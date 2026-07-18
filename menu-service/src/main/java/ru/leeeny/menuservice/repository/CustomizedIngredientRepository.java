package ru.leeeny.menuservice.repository;

import org.springframework.data.domain.Pageable;
import ru.leeeny.menuservice.dto.IngredientSortBy;
import ru.leeeny.menuservice.entity.Ingredient;

import java.util.List;

public interface CustomizedIngredientRepository {

	List<Ingredient> getIngredientsByMenuId(Long menuId, IngredientSortBy sortBy, Pageable pageable);
}
