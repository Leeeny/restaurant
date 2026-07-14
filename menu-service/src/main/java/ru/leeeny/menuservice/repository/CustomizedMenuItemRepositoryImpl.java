package ru.leeeny.menuservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.leeeny.menuservice.dto.SortBy;
import ru.leeeny.menuservice.dto.UpdateMenuRequest;
import ru.leeeny.menuservice.entity.MenuCategory_;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.entity.MenuItem_;
import ru.leeeny.menuservice.repository.updaters.MenuAttrUpdater;

import java.util.List;

@Repository
@AllArgsConstructor
public class CustomizedMenuItemRepositoryImpl implements CustomizedMenuItemRepository {

	private final EntityManager em;

	// Strategy + auto-discovery через DI
	private final List<MenuAttrUpdater<?>> updaters;

	@Override
	@Transactional
	public Integer updateMenuItem(Long id, UpdateMenuRequest dto) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<MenuItem> update = cb.createCriteriaUpdate(MenuItem.class);
		Root<MenuItem> root = update.from(MenuItem.class);
		updaters.forEach(updater -> updater.updateAttr(update, dto));
		update.where(cb.equal(root.get(MenuItem_.id), id));
		return em.createQuery(update).executeUpdate();
	}

	@Override
	public List<MenuItem> getMenusFor(Long categoryId, SortBy sortBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MenuItem> selectQuery = cb.createQuery(MenuItem.class);
		Root<MenuItem> root = selectQuery.from(MenuItem.class);
		selectQuery
				.select(root)
				.where(cb.equal(root.get(MenuItem_.menuCategory).get(MenuCategory_.id), categoryId))
				.orderBy(sortBy.getOrder(cb, root));
		return em.createQuery(selectQuery).getResultList();
	}


}
