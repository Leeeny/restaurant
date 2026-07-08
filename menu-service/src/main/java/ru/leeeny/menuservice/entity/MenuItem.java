package ru.leeeny.menuservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "menu_items", schema = "menu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItem {

	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "menu_item_id_seq"
	)
	@SequenceGenerator(
			name = "menu_item_id_seq",
			sequenceName = "menu.menu_items_id_seq",
			allocationSize = 50
	)
	@Column(
			name = "id",
			nullable = false
	)
	private Long id;

	@Column(
			name = "name",
			length = 128,
			nullable = false,
			unique = true
	)
	private String name;

	@Column(
			name = "description",
			nullable = false,
			columnDefinition = "TEXT"
	)
	private String description;

	@ColumnDefault("true")
	@Column(name = "is_active", nullable = false)
	private Boolean active;

	@Column(
			name = "price",
			nullable = false,
			precision = 10,
			scale = 2
	)
	private BigDecimal price;

	@ManyToOne(
			fetch = FetchType.LAZY,
			optional = false
	)
	@JoinColumn(name = "category_id")
	private MenuCategory menuCategory;

	@Column(
			name = "cook_time_minutes",
			nullable = false
	)
	private Integer cookTimeMinutes;

	@Column(
			name = "weight_grams",
			nullable = false,
			precision = 6,
			scale = 2
	)
	private BigDecimal weightGrams;

	@Column(
			name = "image_url",
			columnDefinition = "TEXT"
	)
	private String imageUrl;

	@Column(
			name = "created_at",
			nullable = false,
			updatable = false
	)
	@CreationTimestamp
	private Instant created;

	@Column(
			name = "updated_at",
			nullable = false
	)
	@UpdateTimestamp
	private Instant updated;

	@OneToMany(
			mappedBy = "menuItem",
			fetch = FetchType.LAZY,
			orphanRemoval = true,
			cascade = CascadeType.ALL
	)
	@Builder.Default
	private Set<MenuItemIngredient> ingredients = new HashSet<>();

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy hp
				? hp.getHibernateLazyInitializer().getPersistentClass()
				: o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy hp
				? hp.getHibernateLazyInitializer().getPersistentClass()
				: this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		MenuItem menuItem = (MenuItem) o;
		return getId() != null && Objects.equals(getId(), menuItem.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy hp
				? hp.getHibernateLazyInitializer().getPersistentClass().hashCode()
				: getClass().hashCode();
	}
}
