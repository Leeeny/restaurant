package ru.leeeny.reviewsservice.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.leeeny.reviewsservice.BaseTest;
import ru.leeeny.reviewsservice.TestcontainersConfiguration;
import ru.leeeny.reviewsservice.config.TransactionOpener;
import ru.leeeny.reviewsservice.entity.Rating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_ONE;
import static ru.leeeny.reviewsservice.testutil.TestConstants.MENU_UNKNOWN;
import static ru.leeeny.reviewsservice.testutil.TestData.ratingMenuOne;
import static ru.leeeny.reviewsservice.testutil.TestUtils.assertRatesEqual;
import static ru.leeeny.reviewsservice.testutil.TestUtils.incrementExpectedRating;

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainersConfiguration.class, TransactionOpener.class})
class RatingRepositoryTest extends BaseTest {

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	TransactionOpener opener;

	@Test
	void incrementRating_incrementsRatingCorrectlyForConcurrentRequests() throws Exception {
		Long menuId = MENU_ONE;
		ExecutorService executor = Executors.newFixedThreadPool(12);

		List<Callable<Void>> incrementors = new ArrayList<>();
		int numIncrementors = 100;
		for (int i = 1; i <= numIncrementors; i++) {
			// по итогу к каждой оценке добавится 20 значений. Так как изначально у оценки rateFive было значение 1, то
			// ее новое значение будет равно 21.
			final int rate = (i % 5 == 0) ? 5 : i % 5;
			incrementors.add(() ->
					opener.runInNewTransaction(() -> {
						ratingRepository.incrementRating(menuId, rate);
						return null;
					})
			);
		}

		var results = executor.invokeAll(incrementors);
		executor.shutdown();

		for (var result : results) {
			assertDoesNotThrow(() -> result.get());
		}
		Rating expectedRating = ratingMenuOne();
		incrementExpectedRating(expectedRating, 20, 20, 20, 20, 20);

		Rating actualRating = ratingRepository.findByMenuId(menuId).get();
		assertRatesEqual(actualRating, expectedRating);
	}

	@Test
	void insertNoConflict_succeeds_whenOneTransactionCommitsAndOneRollsBack() throws Exception {
		Long menuId = MENU_UNKNOWN;
		TransactionDefinition def = new DefaultTransactionDefinition();
		var latch = new CountDownLatch(1);
		Callable<Void> t1 = () -> {
			// стартуем транзакцию
			TransactionStatus status = transactionManager.getTransaction(def);
			// позволяем второму потоку стартовать транзакцию
			latch.countDown();

			ratingRepository.insertNoConflict(menuId);
			transactionManager.rollback(status);
			return null;
		};

		Callable<Void> t2 = () -> {
			// ждем, когда первый поток стартует транзакцию
			latch.await();
			// стартуем транзакцию
			TransactionStatus status = transactionManager.getTransaction(def);
			ratingRepository.insertNoConflict(menuId);
			transactionManager.commit(status);
			return null;
		};

		var results = Executors.newFixedThreadPool(2).invokeAll(Arrays.asList(t2, t1));
		assertDoesNotThrow(() -> {
			results.get(1).get();
			results.get(0).get();
		});

		assertThat(ratingRepository.findByMenuId(menuId))
				.isPresent()
				.hasValueSatisfying(rating -> {
					assertThat(rating.getMenuId()).isEqualTo(menuId);
				});
	}

	@Test
	void insertNoConflict_succeeds_whenTwoConcurrentThreadsPerformInsert() throws Exception {
		Long menuId = MENU_UNKNOWN;
		var latch = new CountDownLatch(1);

		Callable<Void> t1 = () -> {
			latch.countDown();
			opener.runInNewTransaction(
					() -> {
						ratingRepository.insertNoConflict(menuId);
						return null;
					}
			);

			return null;
		};

		Callable<Void> t2 = () -> {
			latch.await();
			opener.runInNewTransaction(
					() -> {
						ratingRepository.insertNoConflict(menuId);
						return null;
					}
			);
			return null;
		};
		var results = Executors.newFixedThreadPool(2).invokeAll(Arrays.asList(t2, t1));

		assertDoesNotThrow(() -> {
			results.get(1).get();
			results.get(0).get();
		});

		assertThat(ratingRepository.findByMenuId(menuId))
				.isPresent()
				.hasValueSatisfying(rating -> {
					assertThat(rating.getMenuId()).isEqualTo(menuId);
				});
	}
}
