package ru.leeeny.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MenuItemDto {

	private Long id;

	private String name;

	private String description;

	private Boolean active;

	private BigDecimal price;

	private Long categoryId;

	private Integer cookTimeMinutes;

	private BigDecimal weightGrams;

	private String imageUrl;

	private Instant created;

	private Instant updated;

	private Set<Long> ingredients;
}

