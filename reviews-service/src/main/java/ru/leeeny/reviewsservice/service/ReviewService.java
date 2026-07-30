package ru.leeeny.reviewsservice.service;

import ru.leeeny.reviewsservice.dto.CreateReviewRequest;
import ru.leeeny.reviewsservice.dto.RatedReviewsResponse;
import ru.leeeny.reviewsservice.dto.ReviewResponse;
import ru.leeeny.reviewsservice.dto.SortBy;

import java.util.List;

public interface ReviewService {

	ReviewResponse createReview(CreateReviewRequest request, String username);

	ReviewResponse getReview(Long reviewId);

	List<ReviewResponse> getReviewsOfUser(String username, SortBy sort, int from, int size);

	RatedReviewsResponse getRatedReviewsForMenu(Long menuId, SortBy sort, int from, int size);
}
