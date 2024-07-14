package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CourseDao;
import it.corso.dao.RoleDao;
import it.corso.dao.UserDao;
import it.corso.dto.CourseDto;
import it.corso.dto.RoleDto;
import it.corso.dto.UserDto;
import it.corso.dto.UserLoginRequestDto;
import it.corso.dto.UserSignupDto;
import it.corso.dto.UserUpdateDto;
import it.corso.model.Course;
import it.corso.model.Role;
import it.corso.model.User;
import it.corso.exceptions.AreadySubscribedException;
import it.corso.exceptions.ObjectNotFoundException;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private CourseDao courseDao;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	//I make my own mapper method because some fields need to be modified
	//between the model object and the dto.
	private UserDto userToDto(User user) {
	    UserDto userDto = modelMapper.map(user, UserDto.class);
	    
	    userDto.setCoursesIds(
	    		user.getCourses().stream()
	    		.map(Course::getId)
	    		.collect(Collectors.toList())
	    		);
	    
	    
	    userDto.setRolesIds(
	    		user.getRoles().stream()
	    		.map(Role::getId)
	    		.collect(Collectors.toList())
	    		);
		    
	    return userDto;
	}	

	@Override
	public void userSignup(UserSignupDto userSignupDto) throws NoSuchElementException {

		User user = new User();
		
		System.out.println("\n\n\n"+ userSignupDto.toString()+"\n\n\n");

		user.setName(userSignupDto.getName());
		user.setLastname(userSignupDto.getLastname());
		user.setMail(userSignupDto.getMail());		
		user.setPassword(DigestUtils.sha256Hex(userSignupDto.getPassword()));
		
		// A list of role ids was passed in the dao
		List<Integer> roleIds = userSignupDto.getRoles();
		
		// if one or more of the ids do not match a role in the database
		// an exception will be thrown and caught by the controller.
		List<Role> roles = roleIds.stream()
				.map(roleId -> roleDao.findById(roleId).get())
				.collect(Collectors.toList());

		user.setRoles(roles);
		
		//save to the db
		userDao.save(user);	
	}

	@Override
	public void updateUserData(UserUpdateDto userUpdateDto) {
		
		Optional<User> optional = userDao.findByMail(userUpdateDto.getMail());
		
		if(optional.isPresent()) {
		
			User updatedUser = (User) optional.get();

			updatedUser.setId(userUpdateDto.getId());
			updatedUser.setName(userUpdateDto.getName());
			updatedUser.setLastname(userUpdateDto.getLastname());
			updatedUser.setMail(userUpdateDto.getMail());
			String password = DigestUtils.sha256Hex(userUpdateDto.getPassword());
			updatedUser.setPassword(password);
			

			List<Integer> roleIds = userUpdateDto.getRoles();
			List<Role> roles = roleIds.stream()
					.map(roleId -> roleDao.findById(roleId).get())
					.collect(Collectors.toList());

			updatedUser.setRoles(roles);
			
			List<Integer> courseIds = userUpdateDto.getCourses();
			
			List<Course> courses = courseIds.stream()
			        .map(courseId -> courseDao.findById(courseId).get())
			        .collect(Collectors.toList());

			updatedUser.setCourses(courses);
			
			userDao.save(updatedUser);
		}
		
	}
	


	@Override
	public void deleteUser(String email) {
		
		Optional<User> optional = userDao.findByMail(email);
		
		if(optional.isPresent())
			userDao.delete(optional.get());
		
	}

	@Override
	public User getUserByMail(String email) throws ObjectNotFoundException {
		
		Optional<User> optional = userDao.findByMail(email);
		
		if(optional.isPresent()) {
			return optional.get();
		}
	        
		
		throw new ObjectNotFoundException();
	}
	


	
	@Override
	public UserDto getUserDtoByMail(String email) throws ObjectNotFoundException  {
		
		Optional<User> optional = userDao.findByMail(email);
		
		if(optional.isPresent()) {
			User user = optional.get();
	        return this.userToDto(user);
		}
		
		throw new ObjectNotFoundException();
	}
	
	@Override
	public List<UserDto> getUsers() {
		List<User> users = (List<User>) userDao.findAll();
        return users.stream()
                    .map(this::userToDto)
                    .collect(Collectors.toList()
                    );
    }
	
	

	@Override
	public boolean existsUserByEmail(String email) {
		return userDao.existsByMail(email);
		
	}

	@Override
	public boolean login(UserLoginRequestDto userLoginRequestDto) {
		
		String encrypted = DigestUtils.sha256Hex(userLoginRequestDto.getPassword());
		
		Optional<User> optional = userDao.findByMailAndPassword(userLoginRequestDto.getEmail(), encrypted);

		if(optional.isPresent()) 
			return true;
		else
			return false;
	}

	@Override
	public void subscribeToCourse(String email, int courseId) throws NoSuchElementException, AreadySubscribedException{
		User user = this.userDao.findByMail(email).get();
		Course course = this.courseDao.findById(courseId).get();
		
		if (user.getCourses().contains(course)) {
			throw new AreadySubscribedException();
		}
		
		user.getCourses().add(course);
		userDao.save(user);
		
	}

	@Override
	public void unsubscribeFromCourse(String email, int courseId) throws NoSuchElementException{
		User user = this.userDao.findByMail(email).get();
		Course course = this.courseDao.findById(courseId).get();
		
		//possibly throw an exception if the user wasn't subbed

		user.getCourses().remove(course);
		userDao.save(user);
		
	}





}
