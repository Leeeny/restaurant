package ru.leeeny.reviewsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.leeeny.reviewsservice.entity.MenuRatingInfo;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatedReviewsResponse {

	private List<ReviewResponse> reviews;

	private MenuRatingInfo menuRating;
}
