package ru.leeeny.menuservice.testcontainers.config.util;

import ru.leeeny.menuservice.dto.UpdateMenuItemDto;

import java.math.BigDecimal;

public class TestData {

	public static UpdateMenuItemDto updateMenuFullRequest() {
		UpdateMenuItemDto updateMenuRequest = new UpdateMenuItemDto();
		updateMenuRequest.setName("New Cappuccino");
		updateMenuRequest.setDescription("New Cappuccino Description");
		updateMenuRequest.setPrice(BigDecimal.valueOf(2D));
		updateMenuRequest.setCookTimeMinutes(5);
		updateMenuRequest.setWeightGrams(BigDecimal.valueOf(150D));
		updateMenuRequest.setImageUrl("http://images.com/new_cappuccino.png");
		updateMenuRequest.setActive(true);
		updateMenuRequest.setCategoryId(19L);
		return updateMenuRequest;
	}

	public static UpdateMenuItemDto updateMenuPartialRequest() {
		UpdateMenuItemDto updateMenuRequest = new UpdateMenuItemDto();
		updateMenuRequest.setName("New Cappuccino");
		updateMenuRequest.setPrice(BigDecimal.valueOf(3D));
		return updateMenuRequest;
	}

}
