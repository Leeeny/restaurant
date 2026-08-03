package ru.leeeny.menuaggregateservice.client;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;
import ru.leeeny.menuaggregateservice.BaseTest;
import ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort;
import ru.leeeny.menuaggregateservice.dto.exception.MenuAggregateException;
import ru.leeeny.menuaggregateservice.dto.menu.Category;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.leeeny.menuaggregateservice.client.MenuClient.MENU_BACKEND;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_ID;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.drinksMenuList;
import static ru.leeeny.menuaggregateservice.testutil.TestDateProvider.menuOneItem;

class MenuClientTest extends BaseTest {

	@Autowired
	private MenuClient client;

	@Test
	void getMenusForCategory_returnsCorrectResponse() {
		stubForCorrectMenuListResponse();

		var mono = client.getMenusForCategory(Category.DRINKS, RatedMenuSort.DATE_DESC);
		StepVerifier.create(mono)
				.expectNextMatches(response ->
						response.equals(drinksMenuList()))
				.verifyComplete();
		wiremock.verify(1, getRequestedFor(urlEqualTo(getMenuListUrl())));
	}

	@Test
	void getMenusForCategory_returnsCorrectResponseAfterRetriesSucceed() {
		stubForMenuItemListWithRetriesAndSuccess();

		var mono = client.getMenusForCategory(Category.DRINKS, RatedMenuSort.DATE_DESC);
		StepVerifier.create(mono)
				.expectNextMatches(response ->
						response.equals(drinksMenuList()))
				.verifyComplete();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getMenuListUrl())));
	}

	@Test
	void getMenusForCategory_returnsErrorOn500Error() {
		stubForMenuItemList500Error();

		var mono = client.getMenusForCategory(Category.DRINKS, RatedMenuSort.DATE_DESC);
		StepVerifier.create(mono)
				.expectError(MenuAggregateException.class)
				.verify();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getMenuListUrl())));
	}

	@Test
	void getMenusForCategory_returnsErrorOnTimeout() {
		stubForMenuItemListTimeout();

		var mono = client.getMenusForCategory(Category.DRINKS, RatedMenuSort.DATE_DESC);
		StepVerifier.create(mono)
				.expectError(MenuAggregateException.class)
				.verify();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getMenuListUrl())));
	}

	@Test
	void getMenusForCategory_returnsErrorAfterRetriesFailWithDifferentReasons() {
		stubForMenuItemListWithRetriesAndFailure();

		var mono = client.getMenusForCategory(Category.DRINKS, RatedMenuSort.DATE_DESC);
		StepVerifier.create(mono)
				.expectError(MenuAggregateException.class)
				.verify();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getMenuListUrl())));
	}

	@Test
	void getMenusForCategory_opensCircuitBreakerAfterAllAttempts() {
		// circuitbreaker minimum-number-of-calls = 6 in tests
		// retry max-attempts = 3
		stubForMenuItemList500Error();
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(MENU_BACKEND);
		for (int i = 1; i <= 4; i++) {
			var mono = client.getMenusForCategory(Category.DRINKS, RatedMenuSort.DATE_DESC);
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
		wiremock.verify(6, getRequestedFor(urlEqualTo(getMenuListUrl())));
	}

	@Test
	void getMenuItem_returnsCorrectResponse() {
		stubForCorrectMenuItemResponse();

		var mono = client.getMenuItem(MENU_ONE_ID);
		StepVerifier.create(mono)
				.expectNextMatches(response ->
						response.equals(menuOneItem()))
				.verifyComplete();
		wiremock.verify(1, getRequestedFor(urlEqualTo(getMenuItemUrl())));
	}

	@Test
	void getMenuItem_returnsCorrectResponseAfterRetriesSucceed() {
		stubForMenuItemWithRetriesAndSuccess();

		var mono = client.getMenuItem(MENU_ONE_ID);
		StepVerifier.create(mono)
				.expectNextMatches(response ->
						response.equals(menuOneItem()))
				.verifyComplete();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getMenuItemUrl())));

	}

	@Test
	void getMenuItem_returnsExceptionOn500Error() {
		stubForMenuItem500Error();

		var mono = client.getMenuItem(MENU_ONE_ID);
		StepVerifier.create(mono)
				.expectError(MenuAggregateException.class)
				.verify();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getMenuItemUrl())));
	}

	@Test
	void getMenuItem_returnsExceptionOnTimeout() {
		stubForMenuItemTimeout();

		var mono = client.getMenuItem(MENU_ONE_ID);
		StepVerifier.create(mono)
				.expectError(MenuAggregateException.class)
				.verify();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getMenuItemUrl())));
	}

	@Test
	void getMenuItem_returnsErrorAfterRetriesFailWithDifferentReasons() {
		stubForMenuItemWithRetriesAndFailure();

		var mono = client.getMenuItem(MENU_ONE_ID);
		StepVerifier.create(mono)
				.expectError(MenuAggregateException.class)
				.verify();
		wiremock.verify(3, getRequestedFor(urlEqualTo(getMenuItemUrl())));
	}

	@Test
	void getMenuItem_opensCircuitBreakerAfterAllAttempts() {
		CircuitBreaker menuCircuitBreaker = circuitBreakerRegistry.circuitBreaker(MENU_BACKEND);
		// circuitbreaker minimum-number-of-calls = 6 in tests
		// retry max-attempts = 3
		stubForMenuItemTimeout();
		for (int i = 1; i <= 4; i++) {
			var mono = client.getMenuItem(MENU_ONE_ID);
			Class<? extends Throwable> expectError;
			if (i < 3) {
				expectError = MenuAggregateException.class;
				assertThat(menuCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
			} else {
				expectError = CallNotPermittedException.class;
				assertThat(menuCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
			}
			StepVerifier.create(mono)
					.expectError(expectError)
					.verify();
		}
		wiremock.verify(6, getRequestedFor(urlEqualTo(getMenuItemUrl())));
	}
}
