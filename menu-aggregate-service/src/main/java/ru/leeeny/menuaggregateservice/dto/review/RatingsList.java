package ru.leeeny.menuaggregateservice.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingsList {

	private List<MenuRatingInfo> menuRatings;

}
