package ru.leeeny.menuservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.leeeny.menuservice.dto.IngredientDto;
import ru.leeeny.menuservice.entity.Ingredient;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IngredientMapper {

	IngredientDto toDto(Ingredient ingredient);

	List<IngredientDto> toDtos(List<Ingredient> ingredients);

/*	@Mapping(target = "id", ignore = true)
	Ingredient toEntity(CreateIngredientDto dto);*/
}
