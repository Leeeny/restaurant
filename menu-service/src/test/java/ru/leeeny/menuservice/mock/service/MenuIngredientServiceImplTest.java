package ru.leeeny.menuservice.mock.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.leeeny.menuservice.dto.IngredientDto;
import ru.leeeny.menuservice.entity.Ingredient;
import ru.leeeny.menuservice.entity.MenuItem;
import ru.leeeny.menuservice.entity.MenuItemIngredient;
import ru.leeeny.menuservice.entity.MenuItemIngredientId;
import ru.leeeny.menuservice.exception.MenuServiceException;
import ru.leeeny.menuservice.mapper.IngredientMapper;
import ru.leeeny.menuservice.repository.IngredientRepository;
import ru.leeeny.menuservice.repository.MenuIngredientRepository;
import ru.leeeny.menuservice.repository.MenuItemRepository;
import ru.leeeny.menuservice.service.impl.MenuIngredientServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuIngredientServiceImplTest {

	@Mock
	private MenuIngredientRepository menuIngredientRepository;

	@Mock
	private MenuItemRepository menuItemRepository;

	@Mock
	private IngredientRepository ingredientRepository;

	@Mock
	private IngredientMapper ingredientMapper;

	@InjectMocks
	private MenuIngredientServiceImpl menuIngredientService;

	private MenuItem menuItem;
	private Ingredient ingredient;
	private IngredientDto ingredientDto;

	@BeforeEach
	void setUp() {
		menuItem = mock(MenuItem.class);
		ingredient = mock(Ingredient.class);
		ingredientDto = mock(IngredientDto.class);
	}

	@Test
	void addIngredient_shouldSaveRelationWithCorrectWeightAndReturnDto() {
		when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
		when(ingredientRepository.findById(2L)).thenReturn(Optional.of(ingredient));
		when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDto);

		IngredientDto result = menuIngredientService.addIngredient(1L, 2L, 150.0);

		ArgumentCaptor<MenuItemIngredient> captor = ArgumentCaptor.forClass(MenuItemIngredient.class);
		verify(menuIngredientRepository).save(captor.capture());

		MenuItemIngredient saved = captor.getValue();
		assertThat(saved.getMenuItem()).isEqualTo(menuItem);
		assertThat(saved.getIngredient()).isEqualTo(ingredient);
		assertThat(saved.getWeightGrams()).isEqualByComparingTo(BigDecimal.valueOf(150.0));
		assertThat(result).isEqualTo(ingredientDto);
	}

	@Test
	void addIngredient_whenGramsIsNull_throwsBadRequest() {
		assertThatThrownBy(() -> menuIngredientService.addIngredient(1L, 2L, null))
				.isInstanceOf(MenuServiceException.class);

		verify(menuIngredientRepository, never()).save(any());
	}

	@Test
	void addIngredient_whenGramsIsNotPositive_throwsBadRequest() {
		assertThatThrownBy(() -> menuIngredientService.addIngredient(1L, 2L, 0.0))
				.isInstanceOf(MenuServiceException.class);

		verify(menuIngredientRepository, never()).save(any());
	}

	@Test
	void addIngredient_whenMenuItemNotFound_throwsNotFound() {
		when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> menuIngredientService.addIngredient(1L, 2L, 100.0))
				.isInstanceOf(MenuServiceException.class)
				.hasMessageContaining("1");

		verify(menuIngredientRepository, never()).save(any());
	}

	@Test
	void addIngredient_whenIngredientNotFound_throwsNotFound() {
		when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
		when(ingredientRepository.findById(2L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> menuIngredientService.addIngredient(1L, 2L, 100.0))
				.isInstanceOf(MenuServiceException.class)
				.hasMessageContaining("2");

		verify(menuIngredientRepository, never()).save(any());
	}

	@Test
	void addIngredient_whenRelationAlreadyExists_currentlyPropagatesRawDbException() {
		MenuItemIngredientId id = new MenuItemIngredientId(1L, 2L);
		when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
		when(ingredientRepository.findById(2L)).thenReturn(Optional.of(ingredient));
		when(menuIngredientRepository.existsById(id)).thenReturn(true);

		assertThatThrownBy(() -> menuIngredientService.addIngredient(1L, 2L, 100.0))
				.isInstanceOf(MenuServiceException.class)
				.hasMessageContaining("2");

		verify(menuIngredientRepository, never()).save(any());
	}

	@Test
	void getIngredients_returnsMappedList() {
		List<Ingredient> ingredients = List.of(ingredient);
		List<IngredientDto> dtos = List.of(ingredientDto);
		when(ingredientRepository.getIngredientsByMenuId(1L)).thenReturn(ingredients);
		when(ingredientMapper.toDtos(ingredients)).thenReturn(dtos);

		List<IngredientDto> result = menuIngredientService.getIngredients(1L);

		assertThat(result).isEqualTo(dtos);
	}

	@Test
	void updateIngredientInMenu_shouldUpdateWeightAndReturnDto() {
		MenuItemIngredientId id = new MenuItemIngredientId(1L, 2L);
		MenuItemIngredient relation = mock(MenuItemIngredient.class);

		when(menuIngredientRepository.findById(id)).thenReturn(Optional.of(relation));
		when(ingredientRepository.findById(2L)).thenReturn(Optional.of(ingredient));
		when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDto);

		IngredientDto result = menuIngredientService.updateIngredientInMenu(1L, 2L, 200.0);

		verify(relation).setWeightGrams(BigDecimal.valueOf(200.0));
		assertThat(result).isEqualTo(ingredientDto);
	}

	@Test
	void updateIngredientInMenu_whenGramsInvalid_throwsBadRequestWithoutTouchingRepositories() {
		assertThatThrownBy(() -> menuIngredientService.updateIngredientInMenu(1L, 2L, -5.0))
				.isInstanceOf(MenuServiceException.class);

		verify(menuIngredientRepository, never()).findById(any());
	}

	@Test
	void updateIngredientInMenu_whenRelationNotFound_throwsNotFound() {
		MenuItemIngredientId id = new MenuItemIngredientId(1L, 99L);
		when(menuIngredientRepository.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> menuIngredientService.updateIngredientInMenu(1L, 99L, 200.0))
				.isInstanceOf(MenuServiceException.class)
				.hasMessageContaining("99");
	}

	@Test
	void deleteIngredient_whenRelationExists_deletesIt() {
		MenuItemIngredientId id = new MenuItemIngredientId(1L, 2L);
		when(menuIngredientRepository.existsById(id)).thenReturn(true);

		menuIngredientService.deleteIngredient(1L, 2L);

		verify(menuIngredientRepository).deleteById(id);
	}

	@Test
	void deleteIngredient_whenRelationDoesNotExist_throwsNotFoundAndDoesNotDelete() {
		MenuItemIngredientId id = new MenuItemIngredientId(1L, 2L);
		when(menuIngredientRepository.existsById(id)).thenReturn(false);

		assertThatThrownBy(() -> menuIngredientService.deleteIngredient(1L, 2L))
				.isInstanceOf(MenuServiceException.class);

		verify(menuIngredientRepository, never()).deleteById(id);
	}
}