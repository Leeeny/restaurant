package ru.leeeny.menuservice.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemIngredientId implements Serializable {

	private Long menuItemId;

	private Long ingredientId;

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
		MenuItemIngredientId that = (MenuItemIngredientId) o;
		return getMenuItemId() != null && Objects.equals(getMenuItemId(), that.getMenuItemId())
				&& getIngredientId() != null && Objects.equals(getIngredientId(), that.getIngredientId());
	}

	@Override
	public final int hashCode() {
		return Objects.hash(menuItemId, ingredientId);
	}
}
