package ru.leeeny.menuaggregateservice.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregate;
import ru.leeeny.menuaggregateservice.dto.aggtegate.MenuAggregateList;
import ru.leeeny.menuaggregateservice.dto.aggtegate.RatedMenuSort;
import ru.leeeny.menuaggregateservice.dto.menu.Category;
import ru.leeeny.menuaggregateservice.dto.review.ReviewSort;
import ru.leeeny.menuaggregateservice.service.AggregateService;

@Tag(name = "MenuAggregateController", description = "REST API для работы с блюдами.")
@Slf4j
@RestController
@RequestMapping("/v1/menu-aggregate")
@RequiredArgsConstructor
public class MenuAggregateController {

	private final AggregateService aggregateService;

	@Operation(
			summary = "${api.get-aggregate.summary}",
			description = "${api.get-aggregate.description}"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "${api.response.getAggregateOk}"),
			@ApiResponse(
					responseCode = "400",
					description = "${api.response.badRequest}",
					content = @Content(
							schema = @Schema(implementation = ProblemDetail.class)
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "${api.response.notFound}",
					content = @Content(
							schema = @Schema(implementation = ProblemDetail.class)
					)
			),
			@ApiResponse(
					responseCode = "503",
					description = "${api.response.serviceUnavailable}",
					content = @Content(
							schema = @Schema(implementation = ProblemDetail.class)
					)
			)
	})
	@GetMapping("/{id}")
	public Mono<MenuAggregate> getMenuAggregateInfo(@PathVariable("id")
	                                                @Positive(message = "Идентификатор блюда не может быть <= 0.")
	                                                Long menuId,
	                                                @RequestParam(value = "sortBy", defaultValue = "date_asc")
	                                                @NotBlank(message = "Параметр сортировки не может быть пустым.")
	                                                String sortBy,
	                                                @RequestParam(value = "from", defaultValue = "0")
	                                                @PositiveOrZero(message = "Начало страницы пагинации не может быть < 0.")
	                                                int from,
	                                                @RequestParam(value = "size", defaultValue = "10")
	                                                @Positive(message = "Размер страницы пагинации не может быть <= 0.")
	                                                int size) {
		log.info("Received request to GET info about menu item with ID={}. Sort: {}, from: {}, size: {}",
				menuId, sortBy, from, size);
		return aggregateService.getMenuAggregateInfo(menuId, ReviewSort.fromString(sortBy), from, size);
	}

	@Operation(
			summary = "${api.get-aggregate-list.summary}",
			description = "${api.get-aggregate-list.description}"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "${api.response.getAggregateListOk}"),
			@ApiResponse(
					responseCode = "503",
					description = "${api.response.serviceUnavailable}",
					content = @Content(
							schema = @Schema(implementation = ProblemDetail.class)
					)
			)
	})
	@GetMapping
	public Mono<MenuAggregateList> getMenusWithRatings(@RequestParam("category")
	                                                   @NotBlank(message = "Категория не может быть пустой.")
	                                                   String category,
	                                                   @RequestParam(value = "sortBy", defaultValue = "rate_desc")
	                                                   @NotBlank(message = "Параметр сортировки не может быть пустым.")
	                                                   String sortBy) {
		log.info("Received request to GET info about menu items for category: {}", category);
		return aggregateService.getMenusWithRatings(Category.fromString(category), RatedMenuSort.fromString(sortBy));
	}
}
