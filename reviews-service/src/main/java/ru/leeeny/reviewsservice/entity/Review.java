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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;
import ru.leeeny.reviewsservice.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(schema = "review", name = "reviews")
@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "menu_id", nullable = false)
	private Long menuId;

	@Column(name = "created_by", nullable = false)
	private String createdBy;

	@Column(name = "comment")
	private String comment;

	@Column(name = "rate", nullable = false)
	private int rate;

	@Column(name = "created_at", nullable = false)
	@CreationTimestamp
	@DateTimeFormat(pattern = DateUtil.DATE_FORMAT)
	private LocalDateTime createdAt;

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
		Review review = (Review) o;
		return getId() != null && Objects.equals(getId(), review.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy hp
				? hp.getHibernateLazyInitializer().getPersistentClass().hashCode()
				: getClass().hashCode();
	}
}
