package ru.leeeny.menuservice.testcontainers.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.leeeny.menuservice.dto.CreateMenuItemIngredientRequest;
import ru.leeeny.menuservice.dto.CreateMenuItemRequest;
import ru.leeeny.menuservice.dto.MenuItemIngredientResponse;
import ru.leeeny.menuservice.dto.MenuItemPageResponse;
import ru.leeeny.menuservice.dto.MenuItemResponse;
import ru.leeeny.menuservice.dto.UpdateMenuItemIngredientRequest;
import ru.leeeny.menuservice.dto.UpdateMenuRequest;
import ru.leeeny.menuservice.testcontainers.config.TestcontainersConfiguration;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
@Sql(
		scripts = "classpath:db/insert-menu.sql",
		executionPhase = BEFORE_TEST_METHOD
)
@Sql(
		scripts = "classpath:db/clear-menus.sql",
		executionPhase = AFTER_TEST_METHOD
)
class MenuItemControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private MenuItemResponse createMenuItem(String name, Long categoryId) {
		CreateMenuItemRequest request = new CreateMenuItemRequest();
		request.setName(name);
		request.setDescription("desc " + name);
		request.setActive(true);
		request.setPrice(499.0);
		request.setCategoryId(categoryId);
		request.setCookTimeMinutes(15);
		request.setWeightGrams(300.0);

		return webTestClient.post()
				.uri("/v1/menu-items")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(MenuItemResponse.class)
				.returnResult()
				.getResponseBody();
	}


	@Test
	void createMenuItem_shouldReturnCreatedItem() {
		CreateMenuItemRequest request = new CreateMenuItemRequest();
		request.setName("Cheeseburger");
		request.setDescription("Classic cheeseburger");
		request.setActive(true);
		request.setPrice(350.0);
		request.setCategoryId(1L);
		request.setCookTimeMinutes(10);
		request.setWeightGrams(250.0);

		webTestClient.post()
				.uri("/v1/menu-items")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.name").isEqualTo("Cheeseburger")
				.jsonPath("$.active").isEqualTo(true)
				.jsonPath("$.categoryId").isEqualTo(1);
	}

	@Test
	void createMenuItem_duplicateName_shouldReturnConflict() {
		createMenuItem("Duplicate", 1L);

		CreateMenuItemRequest request = new CreateMenuItemRequest();
		request.setName("Duplicate");
		request.setDescription("desc");
		request.setActive(true);
		request.setPrice(100.0);
		request.setCategoryId(1L);
		request.setCookTimeMinutes(10);
		request.setWeightGrams(100.0);

		webTestClient.post()
				.uri("/v1/menu-items")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isEqualTo(409);
	}

	@Test
	void createMenuItem_withInvalidData_shouldReturnBadRequest() {
		CreateMenuItemRequest request = new CreateMenuItemRequest();
		// name намеренно не заполнен — ожидаем 400, если в DTO есть @NotBlank/@NotNull валидация.

		webTestClient.post()
				.uri("/v1/menu-items")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void getMenuItemById_shouldReturnItem() {
		MenuItemResponse created = createMenuItem("Fries", 2L);

		webTestClient.get()
				.uri("/v1/menu-items/{id}", created.getId())
				.exchange()
				.expectStatus().isOk()
				.expectBody(MenuItemResponse.class)
				.value(response -> {
					assertThat(response.getId()).isEqualTo(created.getId());
					assertThat(response.getName()).isEqualTo("Fries");
				});
	}

	@Test
	void getMenuItemById_notFound_shouldReturn404() {
		webTestClient.get()
				.uri("/v1/menu-items/{id}", 999_999L)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void updateMenuItem_shouldPatchFields() {
		MenuItemResponse created = createMenuItem("Old Name", 1L);

		UpdateMenuRequest updateRequest = new UpdateMenuRequest();
		updateRequest.setName("New Name");
		updateRequest.setPrice(599.0);

		webTestClient.patch()
				.uri("/v1/menu-items/{id}", created.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(updateRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(created.getId())
				.jsonPath("$.name").isEqualTo("New Name")
				.jsonPath("$.price").isEqualTo(599.0);
	}

	@Test
	void deleteMenuItem_shouldRemoveItem() {
		MenuItemResponse created = createMenuItem("To Delete", 1L);

		webTestClient.delete()
				.uri("/v1/menu-items/{id}", created.getId())
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.get()
				.uri("/v1/menu-items/{id}", created.getId())
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void getMenuItems_shouldReturnPagedList() {
		createMenuItem("Item A", 1L);
		createMenuItem("Item B", 1L);
		createMenuItem("Item C", 2L);

		webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/v1/menu-items")
						.queryParam("page", 0)
						.queryParam("size", 20)
						.build())
				.exchange()
				.expectStatus().isOk()
				.expectBody(MenuItemPageResponse.class)
				.value(page -> {
					Assertions.assertNotNull(page);
					assertThat(page.getContent()).hasSizeGreaterThanOrEqualTo(3);
					assertThat(page.getPage()).isZero();
					assertThat(page.getSize()).isEqualTo(20);
				});
	}

	@Test
	void deleteMenuItem_notFound_shouldReturn404() {
		webTestClient.delete()
				.uri("/v1/menu-items/{id}", 999999L)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void getMenuItems_shouldReturnOnlyActive() {
		webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/v1/menu-items")
						.queryParam("active", true)
						.build())
				.exchange()
				.expectStatus().isOk()
				.expectBody(MenuItemPageResponse.class)
				.value(page ->
						assertThat(page.getContent())
								.allSatisfy(item -> assertThat(item.getActive()).isTrue()));
	}

	@Test
	void getMenuItemsByCategory_shouldFilterByCategory() {
		webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/v1/menu-categories/{categoryId}/menu-items")
						.queryParam("page", 0)
						.queryParam("size", 20)
						.build(10L))
				.exchange()
				.expectStatus().isOk()
				.expectBody(MenuItemPageResponse.class)
				.value(page -> {
					assertThat(page).isNotNull();
					assertThat(page.getContent()).hasSize(2);
					assertThat(page.getContent())
							.extracting(MenuItemResponse::getId)
							.containsExactlyInAnyOrder(17L, 18L);
					assertThat(page.getContent())
							.extracting(MenuItemResponse::getCategoryId)
							.containsOnly(10L);
				});
	}

	@Test
	void getMenuItemsByCategory_unknownCategory_shouldReturnEmptyList() {
		webTestClient.get()
				.uri("/v1/menu-categories/{id}/menu-items", 99999L)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.content.length()").isEqualTo(0);
	}

	@Test
	void addIngredientToMenuItem_shouldReturnCreated() {
		CreateMenuItemIngredientRequest request = new CreateMenuItemIngredientRequest();
		request.setIngredientId(31L);
		request.setWeightGrams(50.0);

		webTestClient.post()
				.uri("/v1/menu-items/{menuItemId}/ingredients", 38L)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(MenuItemIngredientResponse.class)
				.value(response -> {
					Assertions.assertNotNull(response);
					assertThat(response.getIngredientId()).isEqualTo(31L);
					assertThat(response.getIngredientName()).isEqualTo("Tomato");
				});

		Integer count = jdbcTemplate.queryForObject(
				"""
						SELECT COUNT(*)
						FROM menu.menu_item_ingredients
						WHERE menu_item_id = ?
						  AND ingredient_id = ?
						""",
				Integer.class,
				38L,
				31L
		);

		assertThat(count).isEqualTo(1);
	}

	@Test
	void getMenuItemIngredients_shouldReturnList() {
		webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/v1/menu-items/{menuItemId}/ingredients")
						.queryParam("page", 0)
						.queryParam("size", 20)
						.build(38L))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.content.length()").isEqualTo(4)
				.jsonPath("$.content[0].ingredientId").exists();
	}

	@Test
	void updateMenuItemIngredient_shouldReturnUpdatedIngredient() {
		UpdateMenuItemIngredientRequest request = new UpdateMenuItemIngredientRequest();
		request.setWeightGrams(40.0);

		webTestClient.patch()
				.uri("/v1/menu-items/{menuItemId}/ingredients/{ingredientId}", 1L, 14L)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.ingredientId").isEqualTo(14);

		BigDecimal weight = jdbcTemplate.queryForObject(
				"""
						SELECT weight_grams
						FROM menu.menu_item_ingredients
						WHERE menu_item_id = ?
						  AND ingredient_id = ?
						""",
				BigDecimal.class,
				1L,
				14L
		);

		assertThat(weight).isEqualByComparingTo("40.00");
	}

	@Test
	void updateMenuItem_notFound_shouldReturn404() {
		UpdateMenuRequest request = new UpdateMenuRequest();
		request.setName("abc");

		webTestClient.patch()
				.uri("/v1/menu-items/{id}", 999999L)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void removeIngredientFromMenuItem_shouldReturnNoContent() {
		webTestClient.delete()
				.uri("/v1/menu-items/{menuItemId}/ingredients/{ingredientId}", 38L, 41L)
				.exchange()
				.expectStatus().isNoContent();

		Integer count = jdbcTemplate.queryForObject(
				"""
						SELECT COUNT(*)
						FROM menu.menu_item_ingredients
						WHERE menu_item_id = ?
						""",
				Integer.class,
				38L
		);

		assertThat(count).isEqualTo(3);
	}
}