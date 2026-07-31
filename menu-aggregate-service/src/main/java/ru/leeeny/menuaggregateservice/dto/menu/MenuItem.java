package ru.leeeny.menuaggregateservice.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuItem {

	private Long id;

	private String name;

	private String description;

	private BigDecimal price;

	private Category category;

	private long timeToCook;

	private double weight;

	private String imageUrl;

	private LocalDateTime updatedAt;

	private LocalDateTime createdAt;

	private IngredientCollection ingredientCollection;

}
