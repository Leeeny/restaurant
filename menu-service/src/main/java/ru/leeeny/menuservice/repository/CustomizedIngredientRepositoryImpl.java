package ru.leeeny.menuservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.leeeny.menuservice.entity.Ingredient;
import ru.leeeny.menuservice.entity.MenuItemIngredient;
import ru.leeeny.menuservice.entity.MenuItemIngredient_;
import ru.leeeny.menuservice.entity.MenuItem_;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomizedIngredientRepositoryImpl implements CustomizedIngredientRepository {

	private final EntityManager em;

	@Override
	public List<Ingredient> getIngredientsByMenuId(Long menuId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Ingredient> query = cb.createQuery(Ingredient.class);
		Root<MenuItemIngredient> relation = query.from(MenuItemIngredient.class);

		Join<MenuItemIngredient, Ingredient> ingredientJoin = relation.join(MenuItemIngredient_.ingredient);
		query.select(ingredientJoin)
				.where(
						cb.equal(
								relation.get(MenuItemIngredient_.menuItem).get(MenuItem_.id),
								menuId)
				);

		return em.createQuery(query).getResultList();
	}
}
