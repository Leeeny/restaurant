package ru.leeeny.ordersservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import ru.leeeny.ordersservice.entity.converter.MenuLineItemCollectionReadConverter;
import ru.leeeny.ordersservice.entity.converter.MenuLineItemCollectionWriteConverter;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Configuration
@EnableR2dbcRepositories
public class R2dbcConfig {

	@Bean
	public R2dbcCustomConversions r2dbcCustomConversions(ObjectMapper objectMapper) {
		List<Converter<?, ?>> converters = List.of(
				new MenuLineItemCollectionReadConverter(objectMapper),
				new MenuLineItemCollectionWriteConverter(objectMapper)
		);
		return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
	}
}
