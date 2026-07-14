package ru.leeeny.menuservice.repository.updaters;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.leeeny.menuservice.dto.UpdateMenuRequest;
import ru.leeeny.menuservice.entity.MenuCategory;
import ru.leeeny.menuservice.entity.MenuItem_;

import java.math.BigDecimal;

@Configuration
public class MenuAttrUpdaters {

	@Bean
	MenuAttrUpdater<String> name() {
		return new MenuAttrUpdater<>(MenuItem_.name, UpdateMenuRequest::getName);
	}

	@Bean
	MenuAttrUpdater<String> description() {
		return new MenuAttrUpdater<>(MenuItem_.description, UpdateMenuRequest::getDescription);
	}

	@Bean
	MenuAttrUpdater<Boolean> active() {
		return new MenuAttrUpdater<>(MenuItem_.active, UpdateMenuRequest::getActive);
	}

	@Bean
	MenuAttrUpdater<BigDecimal> price() {
		return new MenuAttrUpdater<>(MenuItem_.price, dto ->
				dto.getPrice() == null
						? null
						: BigDecimal.valueOf(dto.getPrice())
		);
	}

	@Bean
	MenuAttrUpdater<MenuCategory> categoryId(EntityManager em) {
		return new MenuAttrUpdater<>(MenuItem_.menuCategory,
				dto -> dto.getCategoryId() == null
						? null
						: em.getReference(MenuCategory.class, dto.getCategoryId())
				// тут будет создан proxy с id и внутри criteria id никогда не будет создан select
		);
	}

	@Bean
	MenuAttrUpdater<Integer> cookTimeMinutes() {
		return new MenuAttrUpdater<>(MenuItem_.cookTimeMinutes, UpdateMenuRequest::getCookTimeMinutes);
	}

	@Bean
	MenuAttrUpdater<BigDecimal> weightGrams() {
		return new MenuAttrUpdater<>(MenuItem_.weightGrams, dto ->
				dto.getWeightGrams() == null
						? null
						: BigDecimal.valueOf(dto.getWeightGrams()));
	}

	@Bean
	MenuAttrUpdater<String> imageUrl() {
		return new MenuAttrUpdater<>(MenuItem_.imageUrl, UpdateMenuRequest::getImageUrl);
	}
}
