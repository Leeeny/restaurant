package ru.leeeny.ordersservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import ru.leeeny.ordersservice.exception.OrderServiceException;

@AllArgsConstructor
public enum SortBy {

	DATE_ASC(Sort.by(Sort.Direction.ASC, "createdAt")),

	DATE_DESC(Sort.by(Sort.Direction.DESC, "createdAt"));

	@Getter
	private final Sort sort;

	@JsonCreator
	public static SortBy fromString(String value) {
		try {
			return SortBy.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException _) {
			var msg = "Failed to crate SortBy from string: %s".formatted(value);
			throw new OrderServiceException(msg, HttpStatus.BAD_REQUEST);
		}
	}
}
