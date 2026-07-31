package ru.leeeny.menuaggregateservice.dto.aggtegate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuAggregateList {

	private List<RatedMenuItem> menuItems;

}
