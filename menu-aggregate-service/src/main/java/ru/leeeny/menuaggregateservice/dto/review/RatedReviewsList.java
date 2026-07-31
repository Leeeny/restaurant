package ru.leeeny.menuaggregateservice.dto.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatedReviewsList {

	private List<Review> reviews;

	private MenuRatingInfo menuRating;

	private ErrorResponse errorResponse;

	public static RatedReviewsList fallbackResponse(Long menuId) {
		return RatedReviewsList.builder()
				.menuRating(MenuRatingInfo.defaultRating(menuId))
				.reviews(List.of())
				.errorResponse(ErrorResponse.builder()
						.message("Не удалось получить данные об отзывах и рейтинге блюда.")
						.reason("Микросервис Review Service недоступен.")
						.build())
				.build();
	}
}
