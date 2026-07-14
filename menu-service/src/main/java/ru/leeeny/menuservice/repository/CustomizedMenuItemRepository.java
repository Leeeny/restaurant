package ru.leeeny.menuservice.repository;

import ru.leeeny.menuservice.dto.SortBy;
import ru.leeeny.menuservice.dto.UpdateMenuRequest;
import ru.leeeny.menuservice.entity.MenuItem;

import java.util.List;

/**
 * Кастомный репозиторий сущности {@link MenuItem}
 *
 * @author Leeeny
 * @since 13.07.2026
 */
public interface CustomizedMenuItemRepository {
	/**
	 * Обновляет поля MenuItem с указанным id в соответствии с полями внутри полученного dto
	 *
	 * @param id  идентификатор обновляемой сущности
	 * @param dto полученный объект с полями для обновления
	 * @return количество обновленных строк
	 */
	Integer updateMenuItem(Long id, UpdateMenuRequest dto);

	/**
	 * Возвращает список блюд указанной категории, отсортированный согласно {@code sortBy}
	 */
	List<MenuItem> getMenusFor(Long categoryId, SortBy sortBy);
}
