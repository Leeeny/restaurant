package ru.leeeny.menuaggregateservice.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "external")
public class ExternalServiceProps {

	private final String menuServiceUrl;

	private final String reviewServiceUrl;

	private final String menuItemPath;

	private final String menuReviewsPath;

	private final String menuRatingsPath;

	private final Duration defaultTimeout;

	private final Duration retryBackoff;

	private final Integer retryCount;

	private final Double retryJitter;

}
