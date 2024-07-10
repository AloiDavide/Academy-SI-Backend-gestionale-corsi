package it.corso.dao;

import java.util.List;

import it.corso.model.Category;
import it.corso.model.Course;

public interface CustomizedCourseRepository {
	 
	 public List<Course> findByNameAndCategoryId(String name, Category category);

	}
