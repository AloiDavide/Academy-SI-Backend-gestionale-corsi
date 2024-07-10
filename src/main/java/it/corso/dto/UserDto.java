package it.corso.dto;

import java.util.List;

public class UserDto {
	private String name;
	private String lastname;
	private String mail;
	private String password;
	private List<Integer> rolesIds;
	private List<Integer> coursesIds;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Integer> getRolesIds() {
		return rolesIds;
	}
	public void setRolesIds(List<Integer> rolesIds) {
		this.rolesIds = rolesIds;
	}
	public List<Integer> getCoursesIds() {
		return coursesIds;
	}
	public void setCoursesIds(List<Integer> coursesIds) {
		this.coursesIds = coursesIds;
	}


}
