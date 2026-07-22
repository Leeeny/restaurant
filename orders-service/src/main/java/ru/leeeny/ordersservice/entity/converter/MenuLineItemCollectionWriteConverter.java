package ru.leeeny.ordersservice.entity.converter;

import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.http.HttpStatus;
import ru.leeeny.ordersservice.entity.MenuLineItem;
import ru.leeeny.ordersservice.exception.OrderServiceException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@WritingConverter
@RequiredArgsConstructor
public class MenuLineItemCollectionWriteConverter implements Converter<List<MenuLineItem>, Json> {

	private final ObjectMapper objectMapper;

	@Override
	public Json convert(@NotNull List<MenuLineItem> menuLineItems) {
		try {
			return Json.of(objectMapper.writeValueAsString(menuLineItems));
		} catch (IllegalArgumentException e) {
			var message = String.format("Error converting MenuLineItemCollection %s to Json: %s", menuLineItems, e.getMessage());
			throw new OrderServiceException(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
