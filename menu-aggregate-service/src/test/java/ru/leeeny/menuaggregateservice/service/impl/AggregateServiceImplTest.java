package ru.leeeny.menuaggregateservice.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.leeeny.menuaggregateservice.BaseTest;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregate;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregateList;
import ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort;
import ru.leeeny.menuaggregateservice.dto.exception.MenuAggregateException;
import ru.leeeny.menuaggregateservice.dto.review.ReviewSort;

import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_ID;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.expectedMenuAggregate;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.expectedMenuAggregateWithFallback;

class AggregateServiceImplTest extends BaseTest {

	@Autowired
	private AggregateServiceImpl aggregateService;

	@Test
	void getMenuAggregateInfo_returnsCorrectResponse() {
		stubForCorrectRatedReviewsList();
		stubForCorrectMenuItemResponse();

		StepVerifier.create(getMenuAggregateMono())
				.expectNextMatches(response ->
						response.equals(expectedMenuAggregate()))
				.verifyComplete();
	}

	@Test
	void getMenuAggregateInfo_returnsErrorWhenMenuServiceUnavailable() {
		stubForCorrectRatedReviewsList();
		stubForMenuItem500Error();

		StepVerifier.create(getMenuAggregateMono())
				.expectError(MenuAggregateException.class)
				.verify();
	}

	@Test
	void getMenuAggregateInfo_returnsFallbackWhenReviewServiceUnavailable() {
		stubForRatedReviewsList500Error();
		stubForCorrectMenuItemResponse();

		StepVerifier.create(getMenuAggregateMono())
				.expectNextMatches(response ->
						response.equals(expectedMenuAggregateWithFallback()))
				.verifyComplete();
	}

	@Test
	void getMenuAggregateInfo_returnsErrorWhenMenuServiceTimedOut() {
		stubForCorrectRatedReviewsList();
		stubForMenuItemTimeout();

		StepVerifier.create(getMenuAggregateMono())
				.expectError(MenuAggregateException.class)
				.verify();
	}

	@Test
	void getMenuAggregateInfo_returnsFallbackWhenReviewServiceTimedOut() {
		stubForRatedReviewsListTimeout();
		stubForCorrectMenuItemResponse();

		StepVerifier.create(getMenuAggregateMono())
				.expectNextMatches(response ->
						response.equals(expectedMenuAggregateWithFallback()))
				.verifyComplete();
	}

/*	@Test //TODO
	void getMenusWithRatings_returnsCorrectResponse() {
		stubForCorrectMenuListResponse();
		stubForCorrectMenuRatingsResponse();

		StepVerifier.create(getMenuRatingsMono())
				.expectNextMatches(response ->
						response.equals(expectedMenuAggregateList(Comparator.comparing(RatedMenuItem::getCreatedAt).reversed())))
				.verifyComplete();
	}*/

	@Test
	void getMenusWithRatings_returnsErrorWhenMenuServiceUnavailable() {
		stubForMenuItemList500Error();
		stubForCorrectMenuRatingsResponse();

		StepVerifier.create(getMenuRatingsMono())
				.expectError(MenuAggregateException.class)
				.verify();
	}

	@Test
	void getMenusWithRatings_returnsErrorWhenReviewServiceUnavailable() {
		stubForCorrectMenuListResponse();
		stubForMenuRatings500Error();

		StepVerifier.create(getMenuRatingsMono())
				.expectError(MenuAggregateException.class)
				.verify();
	}

	@Test
	void getMenusWithRatings_returnsErrorWhenMenuServiceTimedOut() {
		stubForMenuItemListTimeout();
		stubForCorrectMenuRatingsResponse();

		StepVerifier.create(getMenuRatingsMono())
				.expectError(MenuAggregateException.class)
				.verify();
	}

	@Test
	void getMenusWithRatings_returnsErrorWhenReviewServiceTimedOut() {
		stubForCorrectMenuListResponse();
		stubForMenuRatingsTimeout();

		StepVerifier.create(getMenuRatingsMono())
				.expectError(MenuAggregateException.class)
				.verify();
	}

	private Mono<MenuAggregate> getMenuAggregateMono() {
		return aggregateService.getMenuAggregateInfo(MENU_ONE_ID, ReviewSort.DATE_ASC, 0, 10);
	}

	private Mono<MenuAggregateList> getMenuRatingsMono() {
		return aggregateService.getMenusWithRatings(1L, RatedMenuSort.DATE_DESC);
	}
}
