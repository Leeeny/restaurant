package ru.leeeny.menuservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.leeeny.menuservice.dto.CreateMenuItemDto;
import ru.leeeny.menuservice.dto.MenuItemDto;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.entity.MenuItemIngredient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MenuItemMapper {

	@Mapping(target = "categoryId", source = "menuCategory.id")
	@Mapping(target = "ingredients", source = "ingredients")
	MenuItemDto toDto(MenuItem menuItem);

	List<MenuItemDto> toDtos(List<MenuItem> menuItems);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "menuCategory", ignore = true)
	@Mapping(target = "ingredients", ignore = true)
	@Mapping(target = "created", ignore = true)
	@Mapping(target = "updated", ignore = true)
	MenuItem toEntity(CreateMenuItemDto dto);

	default Set<Long> toIngredientIds(Set<MenuItemIngredient> ingredients) {
		if (ingredients == null) {
			return Set.of();
		}

		return ingredients.stream()
				.map(i -> i.getIngredient().getId())
				.collect(Collectors.toSet());
	}
}
