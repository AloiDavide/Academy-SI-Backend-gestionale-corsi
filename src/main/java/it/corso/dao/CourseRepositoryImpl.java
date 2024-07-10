package it.corso.dao;

import java.util.List;

import it.corso.model.Category;
import it.corso.model.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CourseRepositoryImpl implements CustomizedCourseRepository {

	 @PersistenceContext
	 private EntityManager entityManager;
	 
	 public List<Course> findByNameAndCategoryId(String name, Category category) {
	  String sql = "SELECT c FROM Course c WHERE c.name LIKE :name AND c.category = :category";
	  return (List<Course>) entityManager.createQuery(sql)
	    .setParameter("name", name)
	    .setParameter("category", category).getResultList();
	 }

	}
