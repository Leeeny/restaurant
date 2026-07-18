package ru.leeeny.menuservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.leeeny.menuservice.dto.CreateMenuItemDto;
import ru.leeeny.menuservice.dto.CreateMenuItemRequest;
import ru.leeeny.menuservice.dto.MenuItemDto;
import ru.leeeny.menuservice.dto.MenuItemPageResponse;
import ru.leeeny.menuservice.dto.MenuItemResponse;
import ru.leeeny.menuservice.dto.UpdateMenuItemDto;
import ru.leeeny.menuservice.dto.UpdateMenuRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RestMenuItemMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ingredients", ignore = true)
	@Mapping(target = "price", source = "price", qualifiedByName = "doubleToBigDecimal")
	@Mapping(target = "weightGrams", source = "weightGrams", qualifiedByName = "doubleToBigDecimal")
	@Mapping(target = "imageUrl", source = "imageUrl")
	CreateMenuItemDto toServiceDto(CreateMenuItemRequest restDto);

	@Mapping(target = "price", source = "price", qualifiedByName = "doubleToBigDecimal")
	@Mapping(target = "weightGrams", source = "weightGrams", qualifiedByName = "doubleToBigDecimal")
	@Mapping(target = "imageUrl", source = "imageUrl")
	UpdateMenuItemDto toServiceDto(UpdateMenuRequest restDto);

	@Mapping(target = "createdAt", source = "created", qualifiedByName = "instantToOffsetDateTime")
	@Mapping(target = "updatedAt", source = "updated", qualifiedByName = "instantToOffsetDateTime")
	@Mapping(target = "imageUrl", source = "imageUrl")
	MenuItemResponse toRestDto(MenuItemDto serviceDto);

	List<MenuItemResponse> toRestDtos(List<MenuItemDto> serviceDto);

	default MenuItemPageResponse toPageResponse(Page<MenuItemDto> page) {
		MenuItemPageResponse response = new MenuItemPageResponse();

		response.setContent(toRestDtos(page.getContent()));
		response.setPage(page.getNumber());
		response.setSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());

		return response;
	}

	@Named("doubleToBigDecimal")
	default BigDecimal doubleToBigDecimal(Double value) {
		return value != null ? BigDecimal.valueOf(value) : null;
	}

	@Named("instantToOffsetDateTime")
	default OffsetDateTime instantToOffsetDateTime(Instant instant) {
		return instant == null
				? null
				: instant.atOffset(ZoneOffset.UTC);
	}
}
