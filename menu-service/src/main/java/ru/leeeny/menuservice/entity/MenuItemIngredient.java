package ru.leeeny.menuservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "menu_item_ingredients", schema = "menu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemIngredient {

	@EmbeddedId
	private MenuItemIngredientId id;

	@ManyToOne(
			fetch = FetchType.LAZY
	)
	@MapsId("menuItemId")
	@JoinColumn(
			name = "menu_item_id"
	)
	private MenuItem menuItem;

	@ManyToOne(
			fetch = FetchType.LAZY
	)
	@MapsId("ingredientId")
	@JoinColumn(
			name = "ingredient_id"
	)
	private Ingredient ingredient;

	@Column(
			name = "weight_grams",
			nullable = false,
			precision = 6,
			scale = 2
	)
	private BigDecimal weightGrams;

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
		MenuItemIngredient that = (MenuItemIngredient) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return Objects.hash(id);
	}
}
