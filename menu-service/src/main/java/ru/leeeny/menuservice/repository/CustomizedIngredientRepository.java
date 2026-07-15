package ru.leeeny.menuservice.repository;

import ru.leeeny.menuservice.entity.Ingredient;

import java.util.List;

public interface CustomizedIngredientRepository {

	List<Ingredient> getIngredientsByMenuId(Long menuId);
}
