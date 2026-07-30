package ru.leeeny.reviewsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.leeeny.reviewsservice.entity.MenuRatingInfo;
import ru.leeeny.reviewsservice.entity.Rating;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RatingRepository extends JpaRepository<Rating, Long> {

	@Query("""
			SELECT new ru.leeeny.reviewsservice.entity.MenuRatingInfo(
			r.menuId, r.wilsonScore, r.avgStars) FROM Rating r where r.menuId =: menuId
			""")
	Optional<MenuRatingInfo> findRatingInfoByMenuId(@Param("menuId") Long menuId);

	@Query("""
			SELECT new ru.leeeny.reviewsservice.entity.MenuRatingInfo(
			r.menuId, r.wilsonScore, r.avgStars) FROM Rating r where r.menuId in :menuIds
			""")
	List<MenuRatingInfo> findRatingInfosByMenuIdIn(@Param("menuIds") Set<Long> menuIds);

	@Modifying
	@Query(value = """
			UPDATE review.ratings SET
			rate_one = CASE WHEN :rating = 1 THEN rate_one + 1 ELSE rate_one END,
			rate_two = CASE WHEN :rating = 2 THEN rate_two + 1 ELSE rate_two END,
			rate_three = CASE WHEN :rating = 3 THEN rate_three + 1 ELSE rate_three END,
			rate_four = CASE WHEN :rating = 4 THEN rate_four + 1 ELSE rate_four END,
			rate_five = CASE WHEN :rating = 5 THEN rate_five + 1 ELSE rate_five END
			WHERE menu_id = :menuId
			""", nativeQuery = true)
	void incrementRating(@Param("menuId") Long menuId, @Param("rating") Integer rating);

	@Modifying
	@Query(value = """
			INSERT INTO review.ratings(menu_id) values (:menuId) ON CONFLICT DO NOTHING
			""", nativeQuery = true)
	void insertNoConflict(@Param("menuId") Long menuId);

	Optional<Rating> findByMenuId(Long menuId);

}
