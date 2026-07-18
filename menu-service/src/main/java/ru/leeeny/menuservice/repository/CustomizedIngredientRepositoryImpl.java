package ru.leeeny.menuservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.leeeny.menuservice.dto.IngredientSortBy;
import ru.leeeny.menuservice.entity.Ingredient;
import ru.leeeny.menuservice.entity.Ingredient_;
import ru.leeeny.menuservice.entity.MenuItemIngredient;
import ru.leeeny.menuservice.entity.MenuItemIngredient_;
import ru.leeeny.menuservice.entity.MenuItem_;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomizedIngredientRepositoryImpl implements CustomizedIngredientRepository {

	private final EntityManager em;

	@Override
	public List<Ingredient> getIngredientsByMenuId(Long menuId, IngredientSortBy sortBy, Pageable pageable) {
		// TODO: пофиксить два источника сортировки
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Ingredient> query = cb.createQuery(Ingredient.class);
		Root<MenuItemIngredient> relation = query.from(MenuItemIngredient.class);

		Join<MenuItemIngredient, Ingredient> ingredientJoin = relation.join(MenuItemIngredient_.ingredient);
		query.select(ingredientJoin)
				.where(
						cb.equal(
								relation.get(MenuItemIngredient_.menuItem).get(MenuItem_.id),
								menuId)
				)
				.orderBy(toOrder(cb, ingredientJoin, sortBy));
		TypedQuery<Ingredient> typedQuery = em.createQuery(query);
		typedQuery.setFirstResult(Math.toIntExact(pageable.getOffset()));
		typedQuery.setMaxResults(pageable.getPageSize());

		return typedQuery.getResultList();
	}

	private Order toOrder(CriteriaBuilder cb, Join<MenuItemIngredient, Ingredient> ingredientJoin, IngredientSortBy sortBy) {
		return switch (sortBy) {
			case AZ -> cb.asc(ingredientJoin.get(Ingredient_.name));
			case ZA -> cb.desc(ingredientJoin.get(Ingredient_.name));
		};
	}
}
