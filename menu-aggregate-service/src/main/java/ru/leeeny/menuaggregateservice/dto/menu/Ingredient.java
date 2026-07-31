package ru.leeeny.menuaggregateservice.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

	private String name;

	private int calories;

}
