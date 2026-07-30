package ru.leeeny.reviewsservice.testutil;

import ru.leeeny.reviewsservice.entity.Rating;

import java.util.List;

import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_EIGHT;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_FIVE;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_FOUR;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_ONE;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_SEVEN;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_SIX;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_TEN;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_TWO;

public class TestData {

	public static Rating ratingMenuOne() {
		return Rating.newRating(MENU_ONE, 0, 0, 0, 0, 1);
	}

	public static Rating ratingMenuTwo() {
		return Rating.newRating(MENU_TWO);
	}

	public static Rating ratingMenuFour() {
		return Rating.newRating(MENU_FOUR, 0, 0, 0, 0, 1);
	}

	public static Rating ratingMenuFive() {
		return Rating.newRating(MENU_FIVE, 0, 0, 0, 1, 0);
	}

	public static Rating ratingMenuSix() {
		return Rating.newRating(MENU_SIX, 0, 0, 1, 0, 0);
	}

	public static Rating ratingMenuSeven() {
		return Rating.newRating(MENU_SEVEN, 0, 1, 0, 0, 0);
	}

	public static Rating ratingMenuEight() {
		return Rating.newRating(MENU_EIGHT, 1, 0, 0, 0, 0);
	}

	public static Rating ratingMenuTen() {
		return Rating.newRating(MENU_TEN, 1, 1, 1, 1, 1);
	}

	public static List<Rating> allRatingsHaveReviews() {
		return List.of(
				ratingMenuFour(),
				ratingMenuFive(),
				ratingMenuSix(),
				ratingMenuSeven(),
				ratingMenuEight()
		);
	}
}
