package ru.leeeny.menuaggregateservice.client;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.leeeny.menuaggregateservice.BaseTest;
import ru.leeeny.menuaggregateservice.dto.exception.MenuAggregateException;
import ru.leeeny.menuaggregateservice.dto.review.RatedReviewsList;
import ru.leeeny.menuaggregateservice.dto.review.RatingsList;
import ru.leeeny.menuaggregateservice.dto.review.ReviewSort;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_ID;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.REVIEW_BACKEND;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.getRatingsRequest;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.ratedReviewsList;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.ratingsList;

class ReviewsClientTest extends BaseTest {

	@Autowired
	private ReviewsClient client;

	@Test
	void getMenuRatings_returnsCorrectResponse() {
		stubForCorrectMenuRatingsResponse();

		StepVerifier.create(getRatingsListMono())
				.expectNextMatches(response ->
						response.equals(ratingsList()))
				.verifyComplete();
		wiremock.verify(1, postRequestedFor(urlEqualTo(getMenuRatingsUrl())));
	}

	@Test
	void getMenuRatings_returnsCorrectResponseAfterRetries() {
		stubForMenuRatingsRetriesSuccess();

		var mono = getRatingsListMono();
		StepVerifier.create(mono)
				.expectNextMatches(response ->
						response.equals(ratingsList()))
				.verifyComplete();
		wiremock.verify(3, postRequestedFor(urlEqualTo(getMenuRatingsUrl())));
	}

	@Test
	void getMenuRatings_returnsErrorOn500Status() {
		stubForMenuRatings500Error();
		var mono = getRatingsListMono();

		StepVerifier.create(mono)
				.expectError(MenuAggregateException.class)
				.verify();

		wiremock.verify(3, postRequestedFor(urlEqualTo(getMenuRatingsUrl())));
	}

	@Test
	void getMenuRatings_returnsErrorOnTimeout() {
		stubForMenuRatingsTimeout();

		StepVerifier.create(getRatingsListMono())
				.expectError(MenuAggregateException.class)
				.verify();
		wiremock.verify(3, postRequestedFor(urlEqualTo(getMenuRatingsUrl())));
	}

	@Test
	void getMenuRatings_returnsErrorAfterRetries() {
		stubForMenuRatingsRetriesFailure();

		var mono = getRatingsListMono();
		StepVerifier.create(mono)
				.expectError(MenuAggregateException.class)
				.verify();
		wiremock.verify(3, postRequestedFor(urlEqualTo(getMenuRatingsUrl())));
	}

	@Test
	void getMenuRatings_opensCircuitBreakerAfterAllAttempts() {
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(REVIEW_BACKEND);
		// circuitbreaker minimum-number-of-calls = 6 in tests
		// retry max-attempts = 3
		stubForMenuRatings500Error();
		for (int i = 1; i < 5; i++) {
			var mono = getRatingsListMono();
			Class<? extends Throwable> expectError;
			if (i < 3) {
				expectError = MenuAggregateException.class;
				assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
			} else {
				expectError = CallNotPermittedException.class;
				assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
			}
			StepVerifier.create(mono)
					.expectError(expectError)
					.verify();
		}
		wiremock.verify(6, postRequestedFor(urlEqualTo(getMenuRatingsUrl())));
	}

	@Test
	void getReviewsWithMenuRating_returnsCorrectResponse() {
		stubForCorrectRatedReviewsList();

		StepVerifier.create(getRatedReviewListMono())
				.expectNextMatches(response -> {
					return response.equals(ratedReviewsList());
				})
				.verifyComplete();
		wiremock.verify(1, getRequestedFor(urlEqualTo(getReviewsOfMenuOneUrl())));
	}

	@Test
	void getReviewsWithMenuRating_returnsCorrectResponseAfterRetries() {
		stubForRatedReviewsListRetriesSuccess();
		var expected = ratedReviewsList();
		var mono = getRatedReviewListMono();
		StepVerifier.create(mono)
				.expectNextMatches(response -> {
					return response.equals(expected);
				})
				.verifyComplete();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getReviewsOfMenuOneUrl())));
	}

	@Test
	void getReviewsWithMenuRating_returnsFallbackOn500Status() {
		stubForRatedReviewsList500Error();

		StepVerifier.create(getRatedReviewListMono())
				.expectNextMatches(response ->
						response.equals(RatedReviewsList.fallbackResponse(MENU_ONE_ID)))
				.verifyComplete();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getReviewsOfMenuOneUrl())));
	}

	@Test
	void getReviewsWithMenuRating_returnsFallbackOnTimeout() {
		stubForRatedReviewsListTimeout();

		StepVerifier.create(getRatedReviewListMono())
				.expectNextMatches(response ->
						response.equals(RatedReviewsList.fallbackResponse(MENU_ONE_ID)))
				.verifyComplete();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getReviewsOfMenuOneUrl())));
	}

	@Test
	void getReviewsWithMenuRating_returnsFallbackAfterRetries() {
		stubForRatedReviewsListRetriesFailure();

		StepVerifier.create(getRatedReviewListMono())
				.expectNextMatches(response ->
						response.equals(RatedReviewsList.fallbackResponse(MENU_ONE_ID)))
				.verifyComplete();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getReviewsOfMenuOneUrl())));
	}

	@Test
	void getReviewsWithMenuRating_returnsDefault_and_opensCircuitBreakerAfterAllAttempts() {
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(REVIEW_BACKEND);
		// circuitbreaker minimum-number-of-calls = 6 in tests
		// retry max-attempts = 3
		stubForRatedReviewsList500Error();
		for (int i = 1; i < 5; i++) {
			var mono = getRatedReviewListMono();
			if (i < 3) {
				assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
			} else {
				assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
			}
			StepVerifier.create(mono)
					.expectNextMatches(ratedReviewsList ->
							ratedReviewsList.equals(RatedReviewsList.fallbackResponse(MENU_ONE_ID)))
					.verifyComplete();

		}
		wiremock.verify(6, getRequestedFor(urlEqualTo(getReviewsOfMenuOneUrl())));
	}

	private Mono<RatedReviewsList> getRatedReviewListMono() {
		return client.getReviewsWithMenuRating(MENU_ONE_ID, 0, 10, ReviewSort.DATE_ASC);
	}

	private Mono<RatingsList> getRatingsListMono() {
		return client.getMenuRatings(getRatingsRequest());
	}
}
