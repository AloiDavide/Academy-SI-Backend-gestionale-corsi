package it.corso.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dao.CategoryDao;
import it.corso.dao.CourseDao;
import it.corso.dto.CourseDto;
import it.corso.dto.CourseUpdateDto;
import it.corso.dto.UserDto;
import it.corso.model.Category;
import it.corso.model.Course;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDao courseDao;
	@Autowired
	private CategoryDao categoryDao;

	private ModelMapper modelMapper = new ModelMapper();
	
	
	
	
	@Override
    public Course createCourse(CourseDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);
        return courseDao.save(course);
    }
	
	
	@Override
	public Course getCourseById(int id) {
		
		Optional<Course> courseOptional = courseDao.findById(id);
		
		if(courseOptional.isPresent()) {
			return courseOptional.get();
		}
	        
		
		return new Course();
	}
	
	@Override
	public CourseDto getCourseDTOById(int id) {
		Optional<Course> courseOptional = courseDao.findById(id);
		if(!courseOptional.isPresent()) {
			return new CourseDto();
		}                                                                                                              
		
		Course course = courseOptional.get();
        return this.courseToDto(course);
	}
	
	@Override
	public List<CourseDto> getCourses() {
	    List<Course> courses = (List<Course>) courseDao.findAll();
	    return courses.stream()
	                  .map(this::courseToDto)
	                  .collect(Collectors.toList());
	}
	
	//I make my own mapper method because some fields need to be modified
	//between the model object and the dto.
	private CourseDto courseToDto(Course course) {
		CourseDto courseDto = modelMapper.map(course, CourseDto.class);
        courseDto.setCategoryName(course.getCategory().getCategoryName());
        return courseDto;
    }

    @Override
    public void updateCourse(CourseUpdateDto courseUpdateDto) {
        Optional<Course> optional = courseDao.findById(courseUpdateDto.getId());

        if (optional.isPresent()) {
            Course course = optional.get();
            modelMapper.map(courseUpdateDto, course);
            courseDao.save(course);
        }
    }

    @Override
    public void deleteCourse(int id) {
        Optional<Course> optional = courseDao.findById(id);

        if (optional.isPresent()) {
            courseDao.delete(optional.get());
        }
    }

	@Override
	public Collection<CourseDto> searchByNameAndCategoryId(String name, Category category) {
		//TODO
		return null;
	}
	
	  @Override
	  public List<CourseDto> findByCategory(int catId) throws NoSuchElementException {
		Optional<Category> cat = categoryDao.findById(catId);
		
	    List<Course> courses = courseDao.findCourseByCategory(cat.get());
	    
	    
	    return courses.stream()
                .map(this::courseToDto)
                .collect(Collectors.toList());
	    
	    
	  }
	  
	  @Override
	  public void deleteByCategory(int catId) throws NoSuchElementException{
	    Optional<Category> cat = categoryDao.findById(catId);

	    
	    List<Course> courses = courseDao.findCourseByCategory(cat.get());
	    
	    courseDao.deleteAll(courses);
	    
	  }


}
