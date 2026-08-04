package ru.leeeny.menuaggregateservice.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.leeeny.menuaggregateservice.BaseTest;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregate;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregateList;
import ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuItem;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.BASE_URL;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_ID;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.expectedMenuAggregate;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.expectedMenuAggregateList;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.expectedMenuAggregateWithFallback;

@AutoConfigureWebTestClient(timeout = "20000")
class MenuAggregateControllerTest extends BaseTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getMenuAggregateInfo_returnsCorrectResponse() {
		stubForCorrectMenuItemResponse();
		stubForCorrectRatedReviewsList();

		webTestClient.get()
				.uri(BASE_URL + "/" + MENU_ONE_ID)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(MenuAggregate.class)
				.value(response -> {
					assertThat(response).isEqualTo(expectedMenuAggregate());
				});
	}

	@Test
	void getMenuAggregateInfo_returnsNotFoundWhenMenuItemNotFound() {
		stubForMenuItemNotFound();
		stubForCorrectRatedReviewsList();

		webTestClient.get()
				.uri(BASE_URL + "/1000")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void getMenuAggregateInfo_returnsCorrectResponse_withDefaultRatingAndReviewsInfo_whenReviewServiceUnavailable() {
		stubForRatedReviewsList500Error();
		stubForCorrectMenuItemResponse();

		webTestClient.get()
				.uri(BASE_URL + "/" + MENU_ONE_ID)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(MenuAggregate.class)
				.value(response -> {
					assertThat(response).isEqualTo(expectedMenuAggregateWithFallback());
				});
	}

	@Test
	void getMenuAggregateInfo_returns503_whenMenuServiceUnavailable() {
		stubForMenuItem500Error();
		stubForCorrectRatedReviewsList();

		webTestClient.get()
				.uri(BASE_URL + "/" + MENU_ONE_ID)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
	}

	@Test
	void getMenusWithRatings_returnsCorrectResponse() {
		stubForCorrectMenuRatingsResponse();
		stubForCorrectMenuListResponse();

		var expected = expectedMenuAggregateList(Comparator.comparing(RatedMenuItem::getCreatedAt).reversed());

		webTestClient.get()
				.uri(BASE_URL + "?category=DRINKS&sortBy=DATE_DESC")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(MenuAggregateList.class)
				.value(response -> {
					assertThat(response).isEqualTo(expected);
				});
	}

	@Test
	void getMenusWithRatings_returnsCorrectResponse_whenMenuServiceReturnsEmptyList() {
		stubForEmptyMenuRatingsResponse();
		stubForEmptyMenuListResponse();

		webTestClient.get()
				.uri(BASE_URL + "?category=DRINKS&sortBy=DATE_DESC")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(MenuAggregateList.class)
				.value(response -> {
					assertThat(response.getMenuItems()).isEmpty();
				});
	}

	@Test
	void getMenusWithRatings_returns503_whenMenuServiceUnavailable() {
		stubForMenuItemList500Error();
		stubForCorrectMenuRatingsResponse();

		webTestClient.get()
				.uri(BASE_URL + "?category=DRINKS&sortBy=DATE_DESC")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
	}

	@Test
	void getMenusWithRatings_returns503_whenReviewServiceUnavailable() {
		stubForMenuRatings500Error();
		stubForCorrectMenuListResponse();

		webTestClient.get()
				.uri(BASE_URL + "?category=DRINKS&sortBy=DATE_DESC")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
	}
}