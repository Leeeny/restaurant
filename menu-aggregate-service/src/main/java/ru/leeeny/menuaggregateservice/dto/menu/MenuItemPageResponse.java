package ru.leeeny.menuaggregateservice.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuItemPageResponse {

	private List<MenuItem> content = new ArrayList<>();

	private Integer page;

	private Integer size;

	private Long totalElements;

	private Integer totalPages;
}
