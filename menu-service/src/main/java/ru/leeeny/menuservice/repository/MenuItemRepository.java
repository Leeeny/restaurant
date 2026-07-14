package ru.leeeny.menuservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leeeny.menuservice.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>, CustomizedMenuItemRepository {

}
