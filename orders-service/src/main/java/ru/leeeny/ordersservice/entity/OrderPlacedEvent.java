package ru.leeeny.ordersservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "orders_outbox", schema = "orders")
public class OrderPlacedEvent implements Persistable<Long> {

	@Id
	@Column("order_id")
	private Long orderId;

	@Column("created_by")
	private String createdBy;

	@Column("city")
	private String city;

	@Column("street")
	private String street;

	@Column("house")
	private Integer house;

	@Column("apartment")
	private Integer apartment;

	@Column("created_at")
	private LocalDateTime createdAt;


	@Override
	public Long getId() {
		return orderId;
	}

	@Override
	public boolean isNew() {
		return true;
	}
}
