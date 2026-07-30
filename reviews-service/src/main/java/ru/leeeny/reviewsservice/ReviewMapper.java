package ru.leeeny.reviewsservice;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.leeeny.reviewsservice.dto.CreateReviewRequest;
import ru.leeeny.reviewsservice.dto.ReviewResponse;
import ru.leeeny.reviewsservice.entity.Review;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReviewMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(source = "username", target = "createdBy")
	Review toDomain(CreateReviewRequest dto, String username);

	ReviewResponse toReviewResponse(Review review);

	List<ReviewResponse> toReviewResponseList(List<Review> reviews);
}
