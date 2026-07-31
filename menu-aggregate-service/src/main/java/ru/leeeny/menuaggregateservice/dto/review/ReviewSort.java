package ru.leeeny.menuaggregateservice.dto.review;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;
import ru.leeeny.menuaggregateservice.dto.exception.MenuAggregateException;

public enum ReviewSort {

	DATE_ASC,

	DATE_DESC;

	@JsonCreator
	public static ReviewSort fromString(String str) {
		try {
			return ReviewSort.valueOf(str.toUpperCase());
		} catch (IllegalArgumentException e) {
			var msg = "Failed to create ReviewSort from string: %s".formatted(str);
			throw new MenuAggregateException(msg, HttpStatus.BAD_REQUEST);
		}
	}
}
