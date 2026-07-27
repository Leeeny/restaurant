package ru.leeeny.ordersservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMenuInfoResponse {

	List<MenuInfo> menuInfos;

}
