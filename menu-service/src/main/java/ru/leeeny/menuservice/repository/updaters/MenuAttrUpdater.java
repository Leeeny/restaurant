package ru.leeeny.menuservice.repository.updaters;

import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.AllArgsConstructor;
import ru.leeeny.menuservice.dto.UpdateMenuRequest;
import ru.leeeny.menuservice.entity.MenuItem;

import java.util.function.Function;

@AllArgsConstructor
public class MenuAttrUpdater<V> {

	private final SingularAttribute<MenuItem, V> attribute;
	private final Function<UpdateMenuRequest, V> valueExtractor;

	public void updateAttr(CriteriaUpdate<MenuItem> criteria, UpdateMenuRequest dto) {
		V value = valueExtractor.apply(dto);
		if (value != null) {
			criteria.set(attribute, value);
		}
	}
}
