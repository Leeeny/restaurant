package ru.leeeny.menuservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import ru.leeeny.menuservice.entity.MenuCategory_;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.entity.MenuItem_;
import ru.leeeny.menuservice.exception.MenuServiceException;

import java.util.function.Function;

@AllArgsConstructor
public enum SortByEnum {

	AZ(root -> root.get(MenuItem_.name), Direction.ASC),

	ZA(root -> root.get(MenuItem_.name), Direction.DESC),

	PRICE_ASC(root -> root.get(MenuItem_.price), Direction.ASC),

	PRICE_DESC(root -> root.get(MenuItem_.price), Direction.DESC),

	WEIGHT_ASC(root -> root.get(MenuItem_.weightGrams), Direction.ASC),

	WEIGHT_DESC(root -> root.get(MenuItem_.weightGrams), Direction.DESC),

	DATE_ASC(root -> root.get(MenuItem_.created), Direction.ASC),

	DATE_DESC(root -> root.get(MenuItem_.created), Direction.DESC),

	CATEGORY_ASC(root -> root.join(MenuItem_.menuCategory).get(MenuCategory_.name), Direction.ASC),

	CATEGORY_DESC(root -> root.join(MenuItem_.menuCategory).get(MenuCategory_.name), Direction.DESC);

	private final Function<Root<MenuItem>, Expression<? extends Comparable<?>>> pathExpression;
	private final Direction direction;

	public Order getOrder(CriteriaBuilder cb, Root<MenuItem> root) {
		var expr = pathExpression.apply(root);
		return direction == Direction.ASC
				? cb.asc(expr)
				: cb.desc(expr);
	}

	@JsonCreator
	public static SortByEnum fromString(String str) {
		try {
			return SortByEnum.valueOf(str.toUpperCase());
		} catch (IllegalArgumentException _) {
			var msg = "Failed to create SortBy from string6 %s".formatted(str);
			throw new MenuServiceException(msg, HttpStatus.BAD_REQUEST);
		}
	}
}
