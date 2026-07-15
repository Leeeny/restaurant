package ru.leeeny.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class IngredientDto {

	private Long id;

	private String name;

	private BigDecimal caloriesPer100g;

}
