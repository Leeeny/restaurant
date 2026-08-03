package ru.leeeny.menuaggregateservice.testutil;

import ru.leeeny.menuaggregateservice.dto.menu.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestConstants {

	public static final String BASE_URL = "/v1/menu-aggregate";
	public static final Long MENU_ONE_ID = 1L;
	public static final String MENU_ONE_NAME = "One";
	public static final String MENU_ONE_DESCRIPTION = "Nice Item One";
	public static final BigDecimal MENU_ONE_PRICE = BigDecimal.valueOf(10.1);
	public static final Category MENU_ONE_CATEGORY = Category.DRINKS;
	public static final long MENU_ONE_TIME_TO_COOK = 1000;
	public static final double MENU_ONE_WEIGHT = 10.2;
	public static final String MENU_ONE_IMAGE_URL = "http://images.com/one.png";
	public static final LocalDateTime MENU_ONE_UPDATED_AT = LocalDateTime.parse("2024-03-20T11:01:30.043025");
	public static final LocalDateTime MENU_ONE_CREATED_AT = LocalDateTime.parse("2024-03-20T11:01:30.042833");

	public static final Long MENU_TWO_ID = 2L;
	public static final String MENU_TWO_NAME = "Two";
	public static final String MENU_TWO_DESCRIPTION = "Nice Item Two";
	public static final BigDecimal MENU_TWO_PRICE = BigDecimal.valueOf(10.1);
	public static final Category MENU_TWO_CATEGORY = Category.DRINKS;
	public static final long MENU_TWO_TIME_TO_COOK = 1000;
	public static final double MENU_TWO_WEIGHT = 20.2;
	public static final String MENU_TWO_IMAGE_URL = "http://images.com/two.png";
	public static final LocalDateTime MENU_TWO_UPDATED_AT = LocalDateTime.parse("2024-03-20T11:01:34.574555");
	public static final LocalDateTime MENU_TWO_CREATED_AT = LocalDateTime.parse("2024-03-20T11:01:34.574508");

	public static final Long MENU_THREE_ID = 3L;
	public static final String MENU_THREE_NAME = "Three";
	public static final String MENU_THREE_DESCRIPTION = "Nice Item Three";
	public static final BigDecimal MENU_THREE_PRICE = BigDecimal.valueOf(10.1);
	public static final Category MENU_THREE_CATEGORY = Category.DRINKS;
	public static final long MENU_THREE_TIME_TO_COOK = 1000;
	public static final double MENU_THREE_WEIGHT = 30.2;
	public static final String MENU_THREE_IMAGE_URL = "http://images.com/three.png";
	public static final LocalDateTime MENU_THREE_UPDATED_AT = LocalDateTime.parse("2024-03-20T11:01:37.942606");
	public static final LocalDateTime MENU_THREE_CREATED_AT = LocalDateTime.parse("2024-03-20T11:01:37.942576");

	public static final String INGREDIENT_ONE_NAME = "ingredient one";
	public static final String INGREDIENT_TWO_NAME = "ingredient two";
	public static final int INGREDIENT_ONE_CALORIES = 10;
	public static final int INGREDIENT_TWO_CALORIES = 20;

	public static final Float MENU_ONE_WILSON_SCORE = 0.17042015f;
	public static final Float MENU_ONE_AVG_STARS = 3.0f;

	public static final Float MENU_TWO_WILSON_SCORE = 0.2065433f;
	public static final Float MENU_TWO_AVG_STARS = 5.0f;

	public static final Float MENU_THREE_WILSON_SCORE = 0.2065433f;
	public static final Float MENU_THREE_AVG_STARS = 5.0f;

	public static final String ALEX = "Alex";
	public static final LocalDateTime ALEX_REVIEW_DATE = LocalDateTime.parse("2024-03-20T11:01:48.178558");
	public static final Integer ALEX_RATE = 5;

	public static final String JOHN = "John";
	public static final LocalDateTime JOHN_REVIEW_DATE = LocalDateTime.parse("2024-03-20T11:02:17.491651");
	public static final Integer JOHN_RATE = 4;

	public static final String MICHAEL = "Michael";
	public static final LocalDateTime MICHAEL_REVIEW_DATE = LocalDateTime.parse("2024-03-20T11:02:21.307757");
	public static final Integer MICHAEL_RATE = 3;

	public static final String MAX = "Max";
	public static final LocalDateTime MAX_REVIEW_DATE = LocalDateTime.parse("2024-03-20T11:02:25.373142");
	public static final Integer MAX_RATE = 2;

	public static final String PHILL = "Phill";
	public static final LocalDateTime PHILL_REVIEW_DATE = LocalDateTime.parse("2024-03-20T11:02:29.873426");
	public static final Integer PHILL_RATE = 1;

	public static final String MENU_BACKEND = "menuBackend";
	public static final String REVIEW_BACKEND = "reviewBackend";
}
