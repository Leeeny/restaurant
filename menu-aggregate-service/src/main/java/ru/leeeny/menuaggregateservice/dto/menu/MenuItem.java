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

	private Boolean active;

	private BigDecimal price;

	private Long categoryId;

	private Integer cookTimeMinutes;

	private BigDecimal weightGrams;

	private String imageUrl;

	private LocalDateTime updatedAt;

	private LocalDateTime createdAt;

}
