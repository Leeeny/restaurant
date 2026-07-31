package ru.leeeny.menuaggregateservice.dto.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRatingInfo {

	private Long menuId;

	private Float wilsonScore;

	private Float avgStars;

	public static MenuRatingInfo defaultRating(Long menuId) {
		return MenuRatingInfo.builder()
				.menuId(menuId)
				.wilsonScore(0.0f)
				.avgStars(0.0f)
				.build();
	}
}
