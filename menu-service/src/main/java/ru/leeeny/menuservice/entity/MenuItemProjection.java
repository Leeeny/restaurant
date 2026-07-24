package ru.leeeny.menuservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemProjection {

	private String name;

	private BigDecimal price;
}
