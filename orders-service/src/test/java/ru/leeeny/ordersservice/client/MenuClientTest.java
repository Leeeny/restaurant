package ru.leeeny.ordersservice.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.leeeny.ordersservice.dto.GetMenuInfoRequest;
import ru.leeeny.ordersservice.dto.GetMenuInfoResponse;
import ru.leeeny.ordersservice.dto.MenuInfo;
import ru.leeeny.ordersservice.props.OrderServiceProps;
import ru.leeeny.ordersservice.testdata.TestDataProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.leeeny.ordersservice.testdata.TestConstants.DEFAULT_TIMEOUT;
import static ru.leeeny.ordersservice.testdata.TestConstants.DELAY_MILLIS;
import static ru.leeeny.ordersservice.testdata.TestConstants.RETRY_BACKOFF;
import static ru.leeeny.ordersservice.testdata.TestConstants.RETRY_COUNT;
import static ru.leeeny.ordersservice.testdata.TestConstants.RETRY_JITTER;

class MenuClientTest {

	private final OrderServiceProps props = new OrderServiceProps(
			"http://localhost:8091",
			"/v1/menu-items/menu-info",
			DEFAULT_TIMEOUT,
			RETRY_BACKOFF,
			RETRY_COUNT,
			RETRY_JITTER
	);

	private MenuClient menuClient;
	private MockWebServer mockWebServer;

	@BeforeEach
	void setupServer() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
		var webClient = WebClient.builder()
				.baseUrl(mockWebServer.url("/").toString())
				.build();
		menuClient = new MenuClient(webClient, props);
	}

	@AfterEach
	void tearDownServer() throws IOException {
		mockWebServer.shutdown();
	}


	@Test
	void getMenuInfo_returnsInfo_whenReturnsSucceed() throws Exception {
		// на первый запрос ответ сервера - SERVICE_UNAVAILABLE
		mockWebServer.enqueue(new MockResponse().setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value()));
		// на второй запрос ответ сервера с большой задержкой (1500ms)
		mockWebServer.enqueue(TestDataProvider.partialSuccessResponse().setBodyDelay(DELAY_MILLIS, TimeUnit.MILLISECONDS));
		// на третий запрос ответ сервера без задержек
		mockWebServer.enqueue(TestDataProvider.partialSuccessResponse());

		var request = new GetMenuInfoRequest(Set.of("One", "Two", "Three"));
		Mono<GetMenuInfoResponse> response = menuClient.getMenuInfo(request);
		assertResponseCorrect(response);
		verifyNumberOfPostRequests(3);
	}


	@Test
	void getMenuInfo_returnsInfo_whenAllIsOk() throws Exception {
		mockWebServer.enqueue(TestDataProvider.partialSuccessResponse());
		var request = new GetMenuInfoRequest(Set.of("One", "Two", "Three"));
		Mono<GetMenuInfoResponse> response = menuClient.getMenuInfo(request);
		assertResponseCorrect(response);
		verifyNumberOfPostRequests(1);
	}

	private void verifyNumberOfPostRequests(int times) throws Exception {
		for (int i = 0; i < times; i++) {
			// ответы уже готовы и достаются из mockWebServer без задержек
			// делаем timeout, чтобы при некорректной реализации тесты не заблокировались
			RecordedRequest recordedRequest = mockWebServer.takeRequest(1000, TimeUnit.MILLISECONDS);
			assertThat(recordedRequest)
					.as("Recorded requests: %d, expected: %d", i, times)
					.isNotNull();
			assertThat(recordedRequest.getMethod()).isEqualTo("POST");
			assertThat(recordedRequest.getPath()).isEqualTo(props.getMenuInfoPath());
		}
		assertThat(mockWebServer.takeRequest(1000, TimeUnit.MILLISECONDS))
				.as("Expected %d requests, but received more", times).isNull();
	}

	private void assertResponseCorrect(Mono<GetMenuInfoResponse> response) {
		StepVerifier.create(response)
				.expectNextMatches(result -> {
					List<MenuInfo> menuInfos = result.getMenuInfos();
					menuInfos.sort(Comparator.comparing(MenuInfo::getName));
					assertThat(menuInfos)
							.map(MenuInfo::getName)
							.containsExactly("One", "Three", "Two");
					assertThat(menuInfos)
							.map(MenuInfo::getPrice)
							.containsExactly(
									BigDecimal.valueOf(10.1),
									BigDecimal.valueOf(30.3),
									null
							);
					assertThat(menuInfos)
							.map(MenuInfo::getIsAvailable)
							.containsExactly(
									true, true, false
							);
					return true;
				})
				.verifyComplete();
	}
}
