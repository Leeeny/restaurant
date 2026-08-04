package ru.leeeny.menuaggregateservice.testutil;

import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregate;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregateList;
import ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuItem;
import ru.leeeny.menuaggregateservice.dto.menu.Ingredient;
import ru.leeeny.menuaggregateservice.dto.menu.IngredientCollection;
import ru.leeeny.menuaggregateservice.dto.menu.MenuItem;
import ru.leeeny.menuaggregateservice.dto.review.GetRatingsRequest;
import ru.leeeny.menuaggregateservice.dto.review.MenuRatingInfo;
import ru.leeeny.menuaggregateservice.dto.review.RatedReviewsList;
import ru.leeeny.menuaggregateservice.dto.review.RatingsList;
import ru.leeeny.menuaggregateservice.dto.review.Review;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static ru.leeeny.menuaggregateservice.testutil.TestConstants.ALEX;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.ALEX_RATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.ALEX_REVIEW_DATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.INGREDIENT_ONE_CALORIES;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.INGREDIENT_ONE_NAME;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.INGREDIENT_TWO_CALORIES;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.INGREDIENT_TWO_NAME;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.JOHN;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.JOHN_RATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.JOHN_REVIEW_DATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MAX;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MAX_RATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MAX_REVIEW_DATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_AVG_STARS;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_CATEGORY;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_CREATED_AT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_DESCRIPTION;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_ID;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_IMAGE_URL;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_NAME;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_PRICE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_TIME_TO_COOK;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_UPDATED_AT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_WEIGHT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_WILSON_SCORE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_AVG_STARS;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_CATEGORY;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_CREATED_AT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_DESCRIPTION;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_ID;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_IMAGE_URL;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_NAME;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_PRICE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_TIME_TO_COOK;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_UPDATED_AT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_WEIGHT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_THREE_WILSON_SCORE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_AVG_STARS;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_CATEGORY;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_CREATED_AT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_DESCRIPTION;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_ID;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_IMAGE_URL;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_NAME;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_PRICE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_TIME_TO_COOK;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_UPDATED_AT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_WEIGHT;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_TWO_WILSON_SCORE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MICHAEL;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MICHAEL_RATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MICHAEL_REVIEW_DATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.PHILL;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.PHILL_RATE;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.PHILL_REVIEW_DATE;

public class TestDateProvider {

	public static RatedReviewsList ratedReviewsList() {
		return RatedReviewsList.builder()
				.reviews(List.of(
						alexReview(),
						johnReview(),
						michaelReview(),
						maxReview(),
						phillReview()
				))
				.menuRating(menuOneRatingInfo())
				.build();
	}

	public static Review alexReview() {
		return Review.builder()
				.id(1L)
				.menuId(MENU_ONE_ID)
				.createdBy(ALEX)
				.comment("Comment")
				.rate(ALEX_RATE)
				.createdAt(ALEX_REVIEW_DATE)
				.build();
	}

	public static Review johnReview() {
		return Review.builder()
				.id(7L)
				.menuId(MENU_ONE_ID)
				.createdBy(JOHN)
				.comment("Comment")
				.rate(JOHN_RATE)
				.createdAt(JOHN_REVIEW_DATE)
				.build();
	}

	public static Review michaelReview() {
		return Review.builder()
				.id(8L)
				.menuId(MENU_ONE_ID)
				.createdBy(MICHAEL)
				.comment("Comment")
				.rate(MICHAEL_RATE)
				.createdAt(MICHAEL_REVIEW_DATE)
				.build();
	}

	public static Review maxReview() {
		return Review.builder()
				.id(9L)
				.menuId(MENU_ONE_ID)
				.createdBy(MAX)
				.comment("Comment")
				.rate(MAX_RATE)
				.createdAt(MAX_REVIEW_DATE)
				.build();
	}

	public static Review phillReview() {
		return Review.builder()
				.id(10L)
				.menuId(MENU_ONE_ID)
				.createdBy(PHILL)
				.comment("Comment")
				.rate(PHILL_RATE)
				.createdAt(PHILL_REVIEW_DATE)
				.build();
	}

	public static RatingsList ratingsList() {
		return RatingsList.builder()
				.menuRatings(List.of(menuOneRatingInfo(), menuTwoRatingInfo(), menuThreeRatingInfo()))
				.build();
	}

	public static MenuRatingInfo menuOneRatingInfo() {
		return MenuRatingInfo.builder()
				.menuId(MENU_ONE_ID)
				.wilsonScore(MENU_ONE_WILSON_SCORE)
				.avgStars(MENU_ONE_AVG_STARS)
				.build();
	}

	public static MenuRatingInfo menuTwoRatingInfo() {
		return MenuRatingInfo.builder()
				.menuId(MENU_TWO_ID)
				.wilsonScore(MENU_TWO_WILSON_SCORE)
				.avgStars(MENU_TWO_AVG_STARS)
				.build();
	}

	public static MenuRatingInfo menuThreeRatingInfo() {
		return MenuRatingInfo.builder()
				.menuId(MENU_THREE_ID)
				.wilsonScore(MENU_THREE_WILSON_SCORE)
				.avgStars(MENU_THREE_AVG_STARS)
				.build();
	}

	public static List<MenuItem> drinksMenuList() {
		return List.of(
				menuThreeItem(),
				menuTwoItem(),
				menuOneItem()
		);
	}

	public static MenuItem menuOneItem() {
		var item = new MenuItem();
		item.setId(MENU_ONE_ID);
		item.setName(MENU_ONE_NAME);
		item.setDescription(MENU_ONE_DESCRIPTION);
		item.setActive(true);
		item.setPrice(MENU_ONE_PRICE);
		item.setCategoryId(MENU_ONE_CATEGORY);
		item.setCookTimeMinutes(MENU_ONE_TIME_TO_COOK);
		item.setWeightGrams(BigDecimal.valueOf(MENU_ONE_WEIGHT));
		item.setImageUrl(MENU_ONE_IMAGE_URL);
		item.setUpdatedAt(MENU_ONE_UPDATED_AT);
		item.setCreatedAt(MENU_ONE_CREATED_AT);
		return item;
	}

	public static MenuItem menuTwoItem() {
		var item = new MenuItem();
		item.setId(MENU_TWO_ID);
		item.setName(MENU_TWO_NAME);
		item.setDescription(MENU_TWO_DESCRIPTION);
		item.setActive(true);
		item.setPrice(MENU_TWO_PRICE);
		item.setCategoryId(MENU_TWO_CATEGORY);
		item.setCookTimeMinutes(MENU_TWO_TIME_TO_COOK);
		item.setWeightGrams(BigDecimal.valueOf(MENU_TWO_WEIGHT));
		item.setImageUrl(MENU_TWO_IMAGE_URL);
		item.setUpdatedAt(MENU_TWO_UPDATED_AT);
		item.setCreatedAt(MENU_TWO_CREATED_AT);
		return item;
	}

	public static MenuItem menuThreeItem() {
		var item = new MenuItem();
		item.setId(MENU_THREE_ID);
		item.setName(MENU_THREE_NAME);
		item.setDescription(MENU_THREE_DESCRIPTION);
		item.setActive(true);
		item.setPrice(MENU_THREE_PRICE);
		item.setCategoryId(MENU_THREE_CATEGORY);
		item.setCookTimeMinutes(MENU_THREE_TIME_TO_COOK);
		item.setWeightGrams(BigDecimal.valueOf(MENU_THREE_WEIGHT));
		item.setImageUrl(MENU_THREE_IMAGE_URL);
		item.setUpdatedAt(MENU_THREE_UPDATED_AT);
		item.setCreatedAt(MENU_THREE_CREATED_AT);
		return item;
	}

	public static RatedMenuItem menuOneRatedItem() {
		return RatedMenuItem.builder()
				.id(MENU_ONE_ID)
				.name(MENU_ONE_NAME)
				.description(MENU_ONE_DESCRIPTION)
				.active(true)
				.price(MENU_ONE_PRICE)
				.categoryId(MENU_ONE_CATEGORY)
				.cookTimeMinutes(MENU_ONE_TIME_TO_COOK)
				.weightGrams(BigDecimal.valueOf(MENU_ONE_WEIGHT))
				.imageUrl(MENU_ONE_IMAGE_URL)
				.updatedAt(MENU_ONE_UPDATED_AT)
				.createdAt(MENU_ONE_CREATED_AT)
				.wilsonScore(MENU_ONE_WILSON_SCORE)
				.avgStars(MENU_ONE_AVG_STARS)
				.build();
	}

	public static RatedMenuItem menuTwoRatedItem() {
		return RatedMenuItem.builder()
				.id(MENU_TWO_ID)
				.name(MENU_TWO_NAME)
				.description(MENU_TWO_DESCRIPTION)
				.active(true)
				.price(MENU_TWO_PRICE)
				.categoryId(MENU_TWO_CATEGORY)
				.cookTimeMinutes(MENU_TWO_TIME_TO_COOK)
				.weightGrams(BigDecimal.valueOf(MENU_TWO_WEIGHT))
				.imageUrl(MENU_TWO_IMAGE_URL)
				.wilsonScore(MENU_TWO_WILSON_SCORE)
				.avgStars(MENU_TWO_AVG_STARS)
				.build();
	}

	public static RatedMenuItem menuThreeRatedItem() {
		return RatedMenuItem.builder()
				.id(MENU_THREE_ID)
				.name(MENU_THREE_NAME)
				.description(MENU_THREE_DESCRIPTION)
				.active(true)
				.price(MENU_THREE_PRICE)
				.categoryId(MENU_THREE_CATEGORY)
				.cookTimeMinutes(MENU_THREE_TIME_TO_COOK)
				.weightGrams(BigDecimal.valueOf(MENU_THREE_WEIGHT))
				.imageUrl(MENU_THREE_IMAGE_URL)
				.wilsonScore(MENU_THREE_WILSON_SCORE)
				.avgStars(MENU_THREE_AVG_STARS)
				.build();
	}

	public static IngredientCollection ingredientCollection() {
		return new IngredientCollection(
				List.of(
						Ingredient.builder().name(INGREDIENT_ONE_NAME).calories(INGREDIENT_ONE_CALORIES).build(),
						Ingredient.builder().name(INGREDIENT_TWO_NAME).calories(INGREDIENT_TWO_CALORIES).build()
				)
		);
	}

	public static MenuAggregate expectedMenuAggregate() {
		return MenuAggregate.builder()
				.menuItem(menuOneItem())
				.reviews(List.of(alexReview(), johnReview(), michaelReview(), maxReview(), phillReview()))
				.ratingInfo(menuOneRatingInfo())
				.build();
	}

	public static MenuAggregate expectedMenuAggregateWithFallback() {
		RatedReviewsList fallback = RatedReviewsList.fallbackResponse(MENU_ONE_ID);
		return MenuAggregate.builder()
				.menuItem(menuOneItem())
				.reviews(fallback.getReviews())
				.ratingInfo(fallback.getMenuRating())
				.errorResponse(fallback.getErrorResponse())
				.build();
	}

	public static MenuAggregateList expectedMenuAggregateList(Comparator<RatedMenuItem> comparator) {
		var list = new ArrayList<RatedMenuItem>();
		list.add(menuOneRatedItem());
		list.add(menuTwoRatedItem());
		list.add(menuThreeRatedItem());
		list.sort(comparator);
		return MenuAggregateList.builder()
				.menuItems(list)
				.build();
	}

	public static GetRatingsRequest getRatingsRequest() {
		return GetRatingsRequest.builder()
				.menuIds(Set.of(MENU_ONE_ID, MENU_TWO_ID, MENU_THREE_ID))
				.build();
	}
}
