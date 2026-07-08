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
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "ingredients", schema = "menu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "ingredient_id_seq"
	)
	@SequenceGenerator(
			name = "ingredient_id_seq",
			sequenceName = "menu.ingredients_id_seq",
			allocationSize = 50
	)
	@Column(
			name = "id",
			nullable = false
	)
	private Long id;

	@Column(
			name = "name",
			unique = true,
			nullable = false,
			length = 128
	)
	private String name;

	@Column(
			name = "calories_per_100g",
			nullable = false,
			precision = 6,
			scale = 2
	)
	private BigDecimal caloriesPer100g;

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
		Ingredient that = (Ingredient) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy hp
				? hp.getHibernateLazyInitializer().getPersistentClass().hashCode()
				: getClass().hashCode();
	}
}
