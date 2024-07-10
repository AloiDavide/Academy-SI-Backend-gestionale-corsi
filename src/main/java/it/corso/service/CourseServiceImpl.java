package it.corso.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dao.CourseDao;
import it.corso.dto.CourseDto;
import it.corso.dto.CourseUpdateDto;
import it.corso.dto.UserDto;
import it.corso.model.Course;
import it.corso.model.User;

public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDao courseDao;
	@Autowired
    private ModelMapper modelMapper;
	
	@Override
	public CourseDto getCourseDTOById(int id) {
		Optional<Course> courseOptional = courseDao.findById(id);
		if(!courseOptional.isPresent()) {
			return new CourseDto();
		}                                                                                                              
		
		Course course = courseOptional.get();
        return modelMapper.map(course, CourseDto.class);
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
    public Course createCourse(CourseDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);
        return courseDao.save(course);
    }
    
	@Override
	public List<CourseDto> getCourses() {
	    List<Course> courses = (List<Course>) courseDao.findAll();
	    return courses.stream()
	                  .map(course -> modelMapper.map(course, CourseDto.class))
	                  .collect(Collectors.toList());
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

}
