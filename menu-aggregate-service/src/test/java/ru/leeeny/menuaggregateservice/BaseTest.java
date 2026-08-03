package ru.leeeny.menuaggregateservice;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.ResourceUtils;
import ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort;
import ru.leeeny.menuaggregateservice.props.ExternalServiceProps;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static ru.leeeny.menuaggregateservice.dto.menu.Category.DRINKS;
import static ru.leeeny.menuaggregateservice.testutil.TestConstants.MENU_ONE_ID;

@SpringBootTest
public class BaseTest {

	private static final int DELAY = 1500;
	public static final int PORT = 4567;
	private static final String RETRIES_SCENARIO = "Retries Scenario";
	private static final String FAILURE_STEP_1 = "Failure step 1";
	private static final String FAILURE_STEP_2 = "Failure step 2";
	private static final String SUCCESS_STEP = "Success step";

	@Autowired
	private ExternalServiceProps props;
	@Autowired
	protected CircuitBreakerRegistry circuitBreakerRegistry;

	@RegisterExtension
	protected static WireMockExtension wiremock = WireMockExtension.newInstance().options(wireMockConfig().port(PORT)).build();

	@DynamicPropertySource
	static void applyProperties(DynamicPropertyRegistry registry) {
		registry.add("external.menu-service-url", () -> "http://localhost:" + PORT);
		registry.add("external.review-service-url", () -> "http://localhost:" + PORT);
		registry.add("external.retry-backoff", () -> "50ms");
		registry.add("external.retry-count", () -> "3");
		registry.add("external.default-timeout", () -> "1s");
		registry.add("resilience4j.circuitbreaker.configs.default.minimum-number-of-calls", () -> "6");
	}

	@BeforeEach
	void reset() {
		circuitBreakerRegistry.getAllCircuitBreakers().forEach(CircuitBreaker::reset);
	}

	protected void stubForMenuRatingsRetriesFailure() {
		stubForRetriesAndFailure(getMenuRatingsUrl(), "wiremock/ratings-response.json", WireMock::post);
	}

	protected void stubForMenuRatingsRetriesSuccess() {
		stubForRetriesAndSuccess(getMenuRatingsUrl(), "wiremock/ratings-response.json", WireMock::post);
	}

	protected void stubForMenuRatings500Error() {
		stubFor500Error(getMenuRatingsUrl(), WireMock::post);
	}

	protected void stubForMenuRatingsTimeout() {
		stubForTimeout(getMenuRatingsUrl(), WireMock::post);
	}

	protected void stubForCorrectMenuRatingsResponse() {
		stubForCorrectResponse(getMenuRatingsUrl(), WireMock::post, "wiremock/ratings-response.json");
	}

	protected void stubForEmptyMenuRatingsResponse() {
		stubForCorrectResponse(getMenuRatingsUrl(), WireMock::post, "wiremock/ratings-empty-response.json");
	}

	protected void stubForRatedReviewsListRetriesSuccess() {
		stubForRetriesAndSuccess(getReviewsOfMenuOneUrl(), "wiremock/reviews-response.json", WireMock::get);
	}

	protected void stubForRatedReviewsListRetriesFailure() {
		stubForRetriesAndFailure(getReviewsOfMenuOneUrl(), "wiremock/reviews-response.json", WireMock::get);
	}

	protected void stubForRatedReviewsList500Error() {
		stubFor500Error(getReviewsOfMenuOneUrl(), WireMock::get);
	}

	protected void stubForRatedReviewsListTimeout() {
		stubForTimeout(getReviewsOfMenuOneUrl(), WireMock::get);
	}

	protected void stubForCorrectRatedReviewsList() {
		stubForCorrectResponse(getReviewsOfMenuOneUrl(), WireMock::get, "wiremock/reviews-response.json");
	}

	protected void stubForMenuItemListWithRetriesAndSuccess() {
		stubForRetriesAndSuccess(getMenuListUrl(), "wiremock/menu-item-list-response.json", WireMock::get);
	}

	protected void stubForMenuItemListWithRetriesAndFailure() {
		stubForRetriesAndFailure(getMenuListUrl(), "wiremock/menu-item-list-response.json", WireMock::get);
	}

	protected void stubForMenuItemList500Error() {
		stubFor500Error(getMenuListUrl(), WireMock::get);
	}

	protected void stubForMenuItemListTimeout() {
		stubForTimeout(getMenuListUrl(), WireMock::get);
	}

	protected void stubForCorrectMenuListResponse() {
		stubForCorrectResponse(getMenuListUrl(), WireMock::get, "wiremock/menu-item-list-response.json");
	}

	protected void stubForEmptyMenuListResponse() {
		stubForCorrectResponse(getMenuListUrl(), WireMock::get, "wiremock/menu-item-list-empty-response.json");
	}

	protected void stubForMenuItemWithRetriesAndSuccess() {
		stubForRetriesAndSuccess(getMenuItemUrl(), "wiremock/menu-item-response.json", WireMock::get);
	}

	protected void stubForMenuItemWithRetriesAndFailure() {
		stubForRetriesAndFailure(getMenuItemUrl(), "wiremock/menu-item-response.json", WireMock::get);
	}

	protected void stubForMenuItem500Error() {
		stubFor500Error(getMenuItemUrl(), WireMock::get);
	}

	protected void stubForMenuItemTimeout() {
		stubForTimeout(getMenuItemUrl(), WireMock::get);
	}

	protected void stubForMenuItemNotFound() {
		stubFor404Error(getMenuItemUrl(), WireMock::get);
	}

	protected void stubForCorrectMenuItemResponse() {
		stubForCorrectResponse(getMenuItemUrl(), WireMock::get, "wiremock/menu-item-response.json");
	}

	protected String getMenuListUrl() {
		return props.getMenuItemPath() + "?category=" + DRINKS + "&sort=" + RatedMenuSort.DATE_DESC;
	}

	protected String getReviewsOfMenuOneUrl() {
		return props.getMenuReviewsPath() + "/" + MENU_ONE_ID + "?from=0&size=10&sortBy=DATE_ASC";
	}

	protected String getMenuItemUrl() {
		return props.getMenuItemPath() + "/" + MENU_ONE_ID;
	}

	protected String getMenuRatingsUrl() {
		return props.getMenuRatingsPath();
	}

	private void stubForRetriesAndSuccess(String url, String filePath, Function<String, MappingBuilder> function) {
		wiremock.resetScenarios();
		wiremock.stubFor(function.apply(url)
				.inScenario(RETRIES_SCENARIO)
				.whenScenarioStateIs(Scenario.STARTED)
				.willSetStateTo(FAILURE_STEP_1)
				.willReturn(serviceUnavailable()));
		var response = readFileToString(filePath);

		wiremock.stubFor(function.apply(url)
				.inScenario(RETRIES_SCENARIO)
				.whenScenarioStateIs(FAILURE_STEP_1)
				.willSetStateTo(SUCCESS_STEP)
				.willReturn(okJson(response).withFixedDelay(DELAY)));

		wiremock.stubFor(function.apply(url)
				.inScenario(RETRIES_SCENARIO)
				.whenScenarioStateIs(SUCCESS_STEP)
				.willReturn(okJson(response)));
	}

	private void stubForRetriesAndFailure(String url, String filePath, Function<String, MappingBuilder> function) {
		wiremock.resetScenarios();
		wiremock.stubFor(function.apply(url)
				.inScenario(RETRIES_SCENARIO)
				.whenScenarioStateIs(Scenario.STARTED)
				.willSetStateTo(FAILURE_STEP_1)
				.willReturn(serviceUnavailable()));
		var response = readFileToString(filePath);

		wiremock.stubFor(function.apply(url)
				.inScenario(RETRIES_SCENARIO)
				.whenScenarioStateIs(FAILURE_STEP_1)
				.willSetStateTo(FAILURE_STEP_2)
				.willReturn(okJson(response).withFixedDelay(DELAY)));

		wiremock.stubFor(function.apply(url)
				.inScenario(RETRIES_SCENARIO)
				.whenScenarioStateIs(FAILURE_STEP_2)
				.willReturn(serviceUnavailable()));
	}

	private void stubForTimeout(String url, Function<String, MappingBuilder> function) {
		wiremock.stubFor(function.apply(url).willReturn(aResponse().withFixedDelay(DELAY)));
	}

	private void stubFor500Error(String url, Function<String, MappingBuilder> function) {
		wiremock.stubFor(function.apply(url).willReturn(serverError()));
	}

	private void stubFor404Error(String url, Function<String, MappingBuilder> function) {
		wiremock.stubFor(function.apply(url).willReturn(notFound()));
	}

	private void stubForCorrectResponse(String url, Function<String, MappingBuilder> function, String filePath) {
		var response = readFileToString(filePath);
		wiremock.stubFor(function.apply(url).willReturn(okJson(response)));
	}

	private String readFileToString(String filePath) {
		try {
			Path path = ResourceUtils.getFile("classpath:" + filePath).toPath();
			return Files.readString(path, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
