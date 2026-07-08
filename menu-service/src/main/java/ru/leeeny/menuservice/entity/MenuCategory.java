package ru.leeeny.menuservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories", schema = "menu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuCategory { //TODO: подумать позже над тем, чтобы сделать это ENUM
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "menu_category_id_seq"
	)
	@SequenceGenerator(
			name = "menu_category_id_seq",
			sequenceName = "menu.categories_id_seq",
			allocationSize = 50
	)
	@Column(
			name = "id",
			nullable = false
	)
	private Long id;

	@Column(
			name = "name",
			nullable = false,
			unique = true,
			length = 128
	)
	private String name;
}
