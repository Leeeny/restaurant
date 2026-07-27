package ru.leeeny.ordersservice;


import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import ru.leeeny.ordersservice.config.R2dbcConfig;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static ru.leeeny.ordersservice.testdata.TestConstants.DELAY_MILLIS;
import static ru.leeeny.ordersservice.testdata.TestConstants.MENU_INFO_PATH;
import static ru.leeeny.ordersservice.testdata.TestDataProvider.readPartiallySuccessfulResponse;
import static ru.leeeny.ordersservice.testdata.TestDataProvider.readSuccessfulResponse;


@Import({R2dbcConfig.class, TestcontainersConfiguration.class})
@SpringBootTest
@SuppressWarnings({"squid:S2187", "squid:S5786"})
public class BaseIntegrationTest {

	@Autowired
	private ConnectionFactory connectionFactory;

	@BeforeEach
	void populateDb(@Value("classpath:db/insert-orders.sql") Resource script) {
		executeScriptBlocking(script);
	}

	@AfterEach
	void clearDb(@Value("classpath:db/delete-orders.sql") Resource script) {
		executeScriptBlocking(script);
	}

	@RegisterExtension
	protected static WireMockExtension wiremock = WireMockExtension.newInstance()
			.options(wireMockConfig().dynamicPort())
			.build();

	@DynamicPropertySource
	static void applyProperties(DynamicPropertyRegistry registry) {
		registry.add("external.menu-service-url", wiremock::baseUrl);
	}

	protected void prepareStubForServiceUnavailable() {
		wiremock.stubFor(post(MENU_INFO_PATH)
				.willReturn(serviceUnavailable()));
	}

	protected void prepareStubForSuccessWithTimeout() {
		var responseBody = readSuccessfulResponse();
		wiremock.stubFor(post(MENU_INFO_PATH)
				.willReturn(okJson(responseBody).withFixedDelay(DELAY_MILLIS))
		);
	}

	protected void prepareStubForPartialSuccess() {
		var responseBody = readPartiallySuccessfulResponse();
		wiremock.stubFor(post(MENU_INFO_PATH)
				.willReturn(okJson(responseBody))
		);
	}

	protected void prepareStubForSuccess() {
		var responseBody = readSuccessfulResponse();
		wiremock.stubFor(post(MENU_INFO_PATH)
				.willReturn(okJson(responseBody)));
	}

	// https://stackoverflow.com/a/73233121
	private void executeScriptBlocking(final Resource sqlScript) {
		var populator = new ResourceDatabasePopulator();
		populator.addScript(sqlScript);
		populator.populate(connectionFactory).block();
	}
}
