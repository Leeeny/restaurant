package ru.leeeny.ordersservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MenuInfo {

	private String name;

	private BigDecimal price;

	private Boolean isAvailable;

}
