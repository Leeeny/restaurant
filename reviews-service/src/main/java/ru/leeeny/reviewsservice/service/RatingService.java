package ru.leeeny.reviewsservice.service;

import ru.leeeny.reviewsservice.dto.GetRatingsRequest;
import ru.leeeny.reviewsservice.dto.RatingsResponse;
import ru.leeeny.reviewsservice.entity.MenuRatingInfo;

public interface RatingService {

	void saveRating(Long menuId, Integer rate);

	MenuRatingInfo getRatingOfMenu(Long menuId);

	RatingsResponse getRatingsOfMenus(GetRatingsRequest request);
}
