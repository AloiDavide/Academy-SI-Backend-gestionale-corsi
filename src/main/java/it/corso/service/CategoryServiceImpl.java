package it.corso.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.model.Category;
import it.corso.dao.CategoryDao;
import it.corso.dao.UserDao;
import it.corso.dto.CategoryDto;
import it.corso.dto.UserDto;
import it.corso.exceptions.ObjectNotFoundException;
import it.corso.exceptions.UnauthorizedException;
import it.corso.model.User;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryDao categoryDao;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Override
	public List<CategoryDto> getAll() {
		List<Category> categories = (List<Category>) categoryDao.findAll();
        return categories.stream()
                    .map(cat -> modelMapper.map(cat, CategoryDto.class))
                    .collect(Collectors.toList());
	}

	@Override
	public void delete(int id) throws ObjectNotFoundException, UnauthorizedException {
		Optional<Category> categoryOptional = categoryDao.findById(id);
		if (!categoryOptional.isEmpty()) {
			Category category = categoryOptional.get();
			if (!category.getCourses().isEmpty()) {
				categoryDao.delete(category);
			}
			else {
				throw new UnauthorizedException();
			}
		}
		else {
			throw new ObjectNotFoundException();
		}
	}



	
}
