package it.corso.service;

import java.util.List;
import java.util.Optional;

import it.corso.dto.CategoryDto;
import it.corso.exceptions.ObjectNotFoundException;
import it.corso.exceptions.UnauthorizedException;
import it.corso.model.Category;

public interface CategoryService {
	List<CategoryDto> getAll();
	
	void delete(int id) throws ObjectNotFoundException, UnauthorizedException ;

	
	
}
