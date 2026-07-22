package ru.leeeny.ordersservice.entity.converter;

import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.HttpStatus;
import ru.leeeny.ordersservice.entity.MenuLineItem;
import ru.leeeny.ordersservice.exception.OrderServiceException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@ReadingConverter
@RequiredArgsConstructor
public class MenuLineItemCollectionReadConverter implements Converter<Json, List<MenuLineItem>> {

	private final ObjectMapper objectMapper;

	@Override
	public List<MenuLineItem> convert(Json source) {
		try {
			return objectMapper.readValue(source.asArray(), new TypeReference<List<MenuLineItem>>() {
			});
		} catch (JacksonException e) {
			var message = "Failed to convert JSON %s to MenuLineItemCollection: %s".formatted(source.asString(), e.getMessage());
			throw new OrderServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
