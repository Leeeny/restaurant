package ru.leeeny.menuservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leeeny.menuservice.entity.MenuItemIngredient;
import ru.leeeny.menuservice.entity.MenuItemIngredientId;

public interface MenuIngredientRepository extends JpaRepository<MenuItemIngredient, MenuItemIngredientId> {

}
