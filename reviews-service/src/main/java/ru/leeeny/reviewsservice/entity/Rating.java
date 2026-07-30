package ru.leeeny.reviewsservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(schema = "review", name = "ratings")
@Entity
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "menu_id", nullable = false)
	private Long menuId;

	@Column(name = "rate_one", nullable = false)
	private Integer rateOne;

	@Column(name = "rate_two", nullable = false)
	private Integer rateTwo;

	@Column(name = "rate_three", nullable = false)
	private Integer rateThree;

	@Column(name = "rate_four", nullable = false)
	private Integer rateFour;

	@Column(name = "rate_five", nullable = false)
	private Integer rateFive;

	@Column(name = "wilson_score", nullable = false)
	private Float wilsonScore;

	@Column(name = "avg_stars", nullable = false)
	private Float avgStars;

	public static Rating newRating(Long menuId) {
		return newRating(menuId, 0, 0, 0, 0, 0);
	}

	public static Rating newRating(Long menuId, int one, int two, int three, int four, int five) {
		return Rating.builder()
				.id(null)
				.menuId(menuId)
				.rateOne(one)
				.rateTwo(two)
				.rateThree(three)
				.rateFour(four)
				.rateFive(five)
				.wilsonScore(0.0f)
				.avgStars(0.0f)
				.build();
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy
				? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
				: o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
				: this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Rating rating = (Rating) o;
		return getId() != null && Objects.equals(getId(), rating.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy
				? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
				: getClass().hashCode();
	}
}
