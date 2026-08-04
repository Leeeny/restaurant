package ru.leeeny.menuaggregateservice.dto.aggtegate;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.leeeny.menuaggregateservice.dto.menu.MenuItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RatedMenuItem extends MenuItem {

	private Float wilsonScore;

	private Float avgStars;

	@Builder
	public RatedMenuItem(Long id,
	                     String name,
	                     String description,
	                     Boolean active,
	                     BigDecimal price,
	                     Long categoryId,
	                     Integer cookTimeMinutes,
	                     BigDecimal weightGrams,
	                     String imageUrl,
	                     LocalDateTime updatedAt,
	                     LocalDateTime createdAt,
	                     Float wilsonScore,
	                     Float avgStars) {

		super(id, name, description, active, price, categoryId, cookTimeMinutes, weightGrams, imageUrl, updatedAt, createdAt);

		this.wilsonScore = wilsonScore;
		this.avgStars = avgStars;
	}
}
