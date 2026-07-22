package ru.leeeny.ordersservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(schema = "orders", name = "orders")
public class MenuOrder {

	@Id
	private Long id;

	@Column(value = "total_price")
	private BigDecimal totalPrice;

	@Column(value = "city")
	private String city;

	@Column(value = "street")
	private String street;

	@Column(value = "house")
	private int house;

	@Column(value = "apartment")
	private int apartment;

	@Column(value = "menu_line_items")
	private List<MenuLineItem> menuLineItems;

	@Column(value = "status")
	private OrderStatus status;

	@Column(value = "created_by")
	private String createdBy;

	@Column(value = "created_at")
	private LocalDateTime createdAt;

	@Column(value = "updated_at")
	private LocalDateTime updatedAt;
}
