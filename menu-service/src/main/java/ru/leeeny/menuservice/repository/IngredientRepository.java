package ru.leeeny.menuservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leeeny.menuservice.entity.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, CustomizedIngredientRepository {

}
