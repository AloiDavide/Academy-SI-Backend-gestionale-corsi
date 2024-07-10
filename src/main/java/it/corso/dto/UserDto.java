package it.corso.dto;

import java.util.List;

import it.corso.model.Course;
import it.corso.model.Role;

public class UserDto {
	
	private int id;
	private String name;
	private String lastname;
	private String mail;
	private String password;
	private List<RoleDto> roles;
	private List<CourseDto> courses;

	public int getId() {
		return id;
	}
	


	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getMail() {
		return mail;
	}



	public void setMail(String mail) {
		this.mail = mail;
	}



	public List<RoleDto> getRoles() {
		return roles;
	}



	public void setRoles(List<RoleDto> roles) {
		this.roles = roles;
	}



	public String getLastname() {
		return lastname;
	}


	public String getPassword() {
		return password;
	}
	
	public List<RoleDto> getRole() {
		return roles;
	}

	public List<CourseDto> getCourses() {
		return courses;
	}

	public void setId(int id) {
		this.id = id;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(List<RoleDto> role) {
		this.roles = role;
	}

	public void setCourses(List<CourseDto> courses) {
		this.courses = courses;
	}	
	
}
