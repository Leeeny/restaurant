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
public class UpdateMenuItemDto {

	private String name;

	private String description;

	private Boolean active;

	private BigDecimal price;

	private Long categoryId;

	private Integer cookTimeMinutes;

	private BigDecimal weightGrams;

	private String imageUrl;
}