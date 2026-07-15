package ru.leeeny.menuservice.testcontainers.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.leeeny.menuservice.dto.SortByEnum;
import ru.leeeny.menuservice.dto.UpdateMenuItemDto;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.repository.MenuItemRepository;
import ru.leeeny.menuservice.repository.updaters.MenuAttrUpdaters;
import ru.leeeny.menuservice.testcontainers.config.TestcontainersConfiguration;
import ru.leeeny.menuservice.testcontainers.config.util.TestData;
import ru.leeeny.menuservice.testcontainers.config.util.TransactionOpener;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.ReflectionTestUtils.getField;

@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
		scripts = "classpath:db/insert-menu.sql",
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
		scripts = "classpath:db/clear-menus.sql",
		executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)

@Import({MenuAttrUpdaters.class, TestcontainersConfiguration.class, TransactionOpener.class})
class CustomizedMenuItemRepositoryImplTest {

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private EntityManager em;

	@Autowired
	private TransactionOpener transactionOpener;

	@Test
	void updateMenu_updatesMenu_whenAllUpdateFieldsAreSet() {
		var dto = TestData.updateMenuFullRequest();
		var id = getIdByName("Cappuccino");

		int updateCount = transactionOpener.runInNewTransaction(
				() -> menuItemRepository.updateMenuItem(id, dto)
		);

		assertThat(updateCount).isEqualTo(1);
		MenuItem updated = menuItemRepository.findById(id).get();
		assertFieldsEquality(updated, dto,
				"name", "description", "price", "cookTimeMinutes", "weightGrams", "imageUrl", "active"
		);

		assertThat(updated.getMenuCategory().getId()).isEqualTo(dto.getCategoryId());
	}

	@Test
	void updateMenu_updatesOnlySetFields_whenPartOfFieldsAreNull() {
		var dto = TestData.updateMenuPartialRequest();
		var id = getIdByName("Cappuccino");
		MenuItem before = menuItemRepository.findById(id).get();

		int updateCount = transactionOpener.runInNewTransaction(
				() -> menuItemRepository.updateMenuItem(id, dto)
		);

		assertThat(updateCount).isEqualTo(1);
		MenuItem updated = menuItemRepository.findById(id).get();

		// изменённые поля
		assertFieldsEquality(updated, dto, "name", "price");

		// неизменённые поля должны остаться как были ДО апдейта
		assertThat(updated.getDescription()).isEqualTo(before.getDescription());
		assertThat(updated.getImageUrl()).isEqualTo(before.getImageUrl());
		assertThat(updated.getCookTimeMinutes()).isEqualTo(before.getCookTimeMinutes());
		assertThat(updated.getWeightGrams()).isEqualTo(before.getWeightGrams());
		assertThat(updated.getActive()).isEqualTo(before.getActive());
		assertThat(updated.getMenuCategory().getId())
				.isEqualTo(before.getMenuCategory().getId());
	}

	@Test
	void updateMenu_throwsDataIntegrityViolationException_whenNameIsNotUnique() {
		var dto = new UpdateMenuItemDto();
		dto.setName("Homemade Lemonade"); // уже занято id=33
		var id = getIdByName("Fresh Orange Juice"); // id=32

		assertThrows(
				DataIntegrityViolationException.class,
				() -> transactionOpener.runInNewTransaction(
						() -> menuItemRepository.updateMenuItem(id, dto)
				)
		);
	}

	@Test
	void updateMenu_doesNothing_whenMenuItemDoesNotExist() {
		var dto = TestData.updateMenuFullRequest();
		long nonExistentId = 9999L;

		int updateCount = transactionOpener.runInNewTransaction(
				() -> menuItemRepository.updateMenuItem(nonExistentId, dto)
		);

		assertThat(updateCount).isZero();
		assertThat(menuItemRepository.findById(nonExistentId)).isEmpty();
		// side-effect check: остальные строки не задеты
		MenuItem untouched = menuItemRepository.findById(getIdByName("Cappuccino")).get();
		assertThat(untouched.getName()).isEqualTo("Cappuccino");
	}

	@Test
	void getMenusFor_returnsListSortedByPriceAsc_forBeveragesCategory() {
		Long beveragesCategoryId = 18L; // Fresh Orange Juice (220.00), Homemade Lemonade (200.00)

		List<MenuItem> result = menuItemRepository
				.getMenusFor(beveragesCategoryId, SortByEnum.PRICE_ASC);

		assertThat(result)
				.extracting(MenuItem::getName)
				.containsExactly("Homemade Lemonade", "Fresh Orange Juice");
	}


	private Long getIdByName(String name) {
		return em.createQuery("select m.id from MenuItem m where m.name = ?1", Long.class)
				.setParameter(1, name)
				.getSingleResult();
	}

	private <T, R> void assertFieldsEquality(T item, R dto, String... fields) {
		assertFieldsExistence(item, dto, fields);
		assertThat(item).usingRecursiveComparison()
				.comparingOnlyFields(fields)
				.withEqualsForFields(
						(actual, expected) -> toBigDecimal(actual).compareTo(toBigDecimal(expected)) == 0,
						"price", "weightGrams"
				)
				.isEqualTo(dto);
	}

	private static BigDecimal toBigDecimal(Object value) {
		return switch (value) {
			case BigDecimal bd -> bd;
			case Double d -> BigDecimal.valueOf(d);
			case null, default -> throw new IllegalArgumentException(
					"Unsupported type for price/weightGrams comparison: " + value);
		};
	}

	private <T, R> void assertFieldsExistence(T item, R dto, String... fields) {
		boolean itemFieldsMissing = Arrays.stream(fields)
				.anyMatch(field -> getField(item, field) == null);
		boolean dtoFieldsMissing = Arrays.stream(fields)
				.anyMatch(field -> getField(dto, field) == null);

		if (itemFieldsMissing || dtoFieldsMissing) {
			throw new AssertionError(("One or more fields do not exist in the provided objects. " +
					"Actual: %s. Expected: %s. Fields to compare: %s")
					.formatted(item, dto, List.of(fields)));
		}
	}

}