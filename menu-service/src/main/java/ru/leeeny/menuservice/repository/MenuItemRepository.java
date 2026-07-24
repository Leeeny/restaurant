package ru.leeeny.menuservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.entity.MenuItemProjection;

import java.util.List;
import java.util.Set;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>, CustomizedMenuItemRepository {

	@Query("""
			select new ru.leeeny.menuservice.entity.MenuItemProjection(m.name, m.price) from MenuItem m where m.name in :names
			""")
	List<MenuItemProjection> getMenuInfoForNames(Set<String> names);
}
