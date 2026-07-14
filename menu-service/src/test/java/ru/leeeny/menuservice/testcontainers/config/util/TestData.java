package ru.leeeny.menuservice.testcontainers.config.util;

import ru.leeeny.menuservice.dto.UpdateMenuRequest;

public class TestData {

	public static UpdateMenuRequest updateMenuFullRequest() {
		UpdateMenuRequest updateMenuRequest = new UpdateMenuRequest();
		updateMenuRequest.setName("New Cappuccino");
		updateMenuRequest.setDescription("New Cappuccino Description");
		updateMenuRequest.setPrice(2D);
		updateMenuRequest.setCookTimeMinutes(5);
		updateMenuRequest.setWeightGrams(150D);
		updateMenuRequest.setImageUrl("http://images.com/new_cappuccino.png");
		updateMenuRequest.setActive(true);
		updateMenuRequest.setCategoryId(19L);
		return updateMenuRequest;
	}

	public static UpdateMenuRequest updateMenuPartialRequest() {
		UpdateMenuRequest updateMenuRequest = new UpdateMenuRequest();
		updateMenuRequest.setName("New Cappuccino");
		updateMenuRequest.setPrice(3D);
		return updateMenuRequest;
	}

}
