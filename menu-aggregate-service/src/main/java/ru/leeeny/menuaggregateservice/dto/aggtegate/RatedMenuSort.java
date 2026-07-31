package ru.leeeny.menuaggregateservice.dto.aggtegate;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.leeeny.menuaggregateservice.dto.exception.MenuAggregateException;

import java.util.Comparator;

@AllArgsConstructor
public enum RatedMenuSort {

	AZ(Comparator.comparing(RatedMenuItem::getName)),

	ZA(Comparator.comparing(RatedMenuItem::getName).reversed()),

	DATE_ASC(Comparator.comparing(RatedMenuItem::getCreatedAt)),

	DATE_DESC(Comparator.comparing(RatedMenuItem::getCreatedAt).reversed()),

	PRICE_ASC(Comparator.comparing(RatedMenuItem::getPrice)),

	PRICE_DESC(Comparator.comparing(RatedMenuItem::getPrice).reversed()),

	RATE_ASC(Comparator.comparing(RatedMenuItem::getWilsonScore)),

	RATE_DESC(Comparator.comparing(RatedMenuItem::getWilsonScore).reversed());

	@JsonCreator
	public static RatedMenuSort fromString(String str) {
		try {
			return RatedMenuSort.valueOf(str.toUpperCase());
		} catch (IllegalArgumentException _) {
			var msg = "Failed to create MenuSort from string: %s".formatted(str);
			throw new MenuAggregateException(msg, HttpStatus.BAD_REQUEST);
		}
	}

	@Getter
	private final Comparator<RatedMenuItem> comparator;
}
