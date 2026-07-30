package ru.leeeny.reviewsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.leeeny.reviewsservice.config.TransactionOpener;
import ru.leeeny.reviewsservice.entity.MenuRatingInfo;
import ru.leeeny.reviewsservice.entity.Rating;
import ru.leeeny.reviewsservice.repository.RatingRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static ru.leeeny.reviewsservice.testutil.TestUtils.incrementExpectedRating;

@ActiveProfiles("test")
@Sql(
		scripts = "classpath:db/insert-data.sql",
		executionPhase = BEFORE_TEST_METHOD
)
@Sql(
		scripts = "classpath:db/delete-data.sql",
		executionPhase = AFTER_TEST_METHOD
)
@Import(TransactionOpener.class)
public abstract class BaseTest {

	@Autowired
	protected RatingRepository ratingRepository;

	@Autowired
	protected TransactionOpener transactionOpener;

	protected void incrementRatingsForMenuId(Rating expected,
	                                         int oneTimes,
	                                         int twoTimes,
	                                         int threeTimes,
	                                         int fourTimes,
	                                         int fiveTimes) {
		incrementActualRatingsForMenuId(expected.getMenuId(), oneTimes, twoTimes, threeTimes, fourTimes, fiveTimes);
		incrementExpectedRating(expected, oneTimes, twoTimes, threeTimes, fourTimes, fiveTimes);
	}

	protected void incrementActualRatingsForMenuId(Long menuId,
	                                               int oneTimes,
	                                               int twoTimes,
	                                               int threeTimes,
	                                               int fourTimes,
	                                               int fiveTimes) {
		incrementRatingForMenuId(menuId, 1, oneTimes);
		incrementRatingForMenuId(menuId, 2, twoTimes);
		incrementRatingForMenuId(menuId, 3, threeTimes);
		incrementRatingForMenuId(menuId, 4, fourTimes);
		incrementRatingForMenuId(menuId, 5, fiveTimes);
	}

	private void incrementRatingForMenuId(Long menuId,
	                                      Integer rating,
	                                      int times) {
		for (int i = 0; i < times; i++) {
			transactionOpener.runInNewTransaction(() -> {
				ratingRepository.incrementRating(menuId, rating);
				return null;
			});
		}
	}

	protected void compareDefaultMenuInfo(Long noRatingMenu, MenuRatingInfo rating) {
		Float zero = 0.0f;
		assertThat(rating.getAvgStars()).isEqualTo(zero);
		assertThat(rating.getWilsonScore()).isEqualTo(zero);
		assertThat(rating.getMenuId()).isEqualTo(noRatingMenu);
	}
}
