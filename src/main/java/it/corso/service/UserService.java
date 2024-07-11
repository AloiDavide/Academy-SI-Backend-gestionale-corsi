package it.corso.service;

import java.util.List;

import it.corso.dto.UserDto;
import it.corso.dto.UserLoginRequestDto;
import it.corso.dto.UserSignupDto;
import it.corso.dto.UserUpdateDto;
import it.corso.model.User;

public interface UserService {
	User getUserByMail(String email); 
	UserDto getUserDtoByMail(String email);
	List<UserDto> getUsers();
	void updateUserData(UserUpdateDto userUpdateDto);
	void deleteUser(String email);
	boolean existsUserByEmail(String email);
	boolean login(UserLoginRequestDto userLoginRequestDto);
	void userSignup(UserSignupDto userSignupDto);
	void subscribeToCourse(String email, int courseId);
	
}
