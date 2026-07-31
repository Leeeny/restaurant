package ru.leeeny.menuaggregateservice.dto.aggtegate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.ErrorResponse;
import ru.leeeny.menuaggregateservice.dto.menu.MenuItem;
import ru.leeeny.menuaggregateservice.dto.review.MenuRatingInfo;
import ru.leeeny.menuaggregateservice.dto.review.Review;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuAggregate {

	private MenuItem menuItem;

	private List<Review> reviews;

	private MenuRatingInfo ratingInfo;

	private ErrorResponse errorResponse;
}
