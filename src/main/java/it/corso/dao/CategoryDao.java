package it.corso.dao;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Category;

public interface CategoryDao extends CrudRepository<Category, Integer>{

}
