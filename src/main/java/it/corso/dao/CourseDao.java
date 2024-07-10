package it.corso.dao;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Course;

public interface CourseDao  extends CrudRepository<Course, Integer>{
	
	
}
