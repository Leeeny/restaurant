package ru.leeeny.menuservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.leeeny.menuservice.dto.IngredientDto;
import ru.leeeny.menuservice.dto.MenuItemIngredientResponse;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RestMenuItemIngredientMapper {

	@Mapping(target = "ingredientId", source = "id")
	@Mapping(target = "ingredientName", source = "name")
	@Mapping(target = "caloriesPer100g", source = "caloriesPer100g", qualifiedByName = "bigDecimalToDouble")
	MenuItemIngredientResponse toRestDto(IngredientDto dto);

	List<MenuItemIngredientResponse> toRestDtos(List<IngredientDto> dto);

	@Named("bigDecimalToDouble")
	default Double bigDecimalToDouble(BigDecimal value) {
		return value != null ? value.doubleValue() : null;
	}
}
