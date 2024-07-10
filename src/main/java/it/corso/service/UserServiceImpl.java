package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.UserDao;
import it.corso.dto.UserDto;
import it.corso.dto.UserLoginRequestDto;
import it.corso.dto.UserSignupDto;
import it.corso.dto.UserUpdateDto;
import it.corso.model.Role;
import it.corso.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public void userSignup(UserSignupDto userSignupDto) {

		User user = new User();

		user.setName(userSignupDto.getFirstname());
		user.setLastname(userSignupDto.getLastname());
		user.setMail(userSignupDto.getEmail());		
		user.setPassword(DigestUtils.sha256Hex(userSignupDto.getPassword()));
		
		
		//TODO estrarre i ruoli corrispondenti all'utente con un roleDao
		
		//List<RoleDto> signupRoles = userSignupDto.getRoles();
		
		//List<Role> databaseRoles = (List<User>) roleDao.??????;
		//user.setRoles(databaseRoles);
		
		
		//TODO applicare lo stesso trattamento ai corsi
		
		userDao.save(user);	
	}

	@Override
	public void updateUserData(UserUpdateDto userUpdateDto) {
		
		Optional<User> optional = userDao.findByMail(userUpdateDto.getEmail());
		
		if(optional.isPresent()) {
		
			User userDb = (User) optional.get();

			userDb.setId(userUpdateDto.getId());
			userDb.setName(userUpdateDto.getFirstname());
			userDb.setLastname(userUpdateDto.getLastname());
			userDb.setMail(userUpdateDto.getEmail());
			String password = DigestUtils.sha256Hex(userUpdateDto.getPassword());
			userDb.setPassword(password);
			
			//TODO permettere un update del ruolo in modo
			//simile al sign up (tramite RoleDao)
			
			userDao.save(userDb);
		}
		
	}
	


	@Override
	public void deleteUser(String email) {
		
		Optional<User> optional = userDao.findByMail(email);
		
		if(optional.isPresent())
			userDao.delete(optional.get());
		
	}

	@Override
	public User getUserByMail(String email) {
		
		Optional<User> optional = userDao.findByMail(email);
		
		if(optional.isPresent())
	        return optional.get();
		
		return new User();
	}
	
	@Override
	public UserDto getUserDtoByMail(String email) {
		
		Optional<User> optional = userDao.findByMail(email);
		
		if(optional.isPresent()) {
			User user = optional.get();
	        return modelMapper.map(user, UserDto.class);
		}
		
		return new UserDto();
	}
	
	@Override
	public List<UserDto> getUsers() {
		List<User> users = (List<User>) userDao.findAll();
        return users.stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
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




}
