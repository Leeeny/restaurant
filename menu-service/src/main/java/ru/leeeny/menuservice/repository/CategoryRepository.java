package ru.leeeny.menuservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.leeeny.menuservice.entity.MenuCategory;

public interface CategoryRepository extends JpaRepository<MenuCategory, Long> {
}
