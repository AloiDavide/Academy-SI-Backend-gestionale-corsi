package it.corso.service;
import java.util.List;

import it.corso.dto.CourseDto;
import it.corso.dto.CourseUpdateDto;
import it.corso.model.Course;

public interface CourseService {
	Course getCourseById(int id); 
	List<CourseDto> getCourses();
	Course createCourse(CourseDto course);
	void updateCourse(CourseUpdateDto corso);
	void deleteCourse(int id);
	CourseDto getCourseDTOById(int id);
}
