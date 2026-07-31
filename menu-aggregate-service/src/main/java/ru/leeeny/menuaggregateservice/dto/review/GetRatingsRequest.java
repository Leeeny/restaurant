package ru.leeeny.menuaggregateservice.dto.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRatingsRequest {

	private Set<Long> menuIds;

}
