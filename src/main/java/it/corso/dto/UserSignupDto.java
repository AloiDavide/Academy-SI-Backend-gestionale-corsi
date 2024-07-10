package it.corso.dto;

import java.util.List;

public class UserSignupDto {
	private String name;
	private String lastname;
	private String mail;
	private String password;
	private List<Integer> roles;
	
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
	public List<Integer> getRoles() {
		return roles;
	}
	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "UserSignupDto [name=" + name + ", lastname=" + lastname + ", mail=" + mail + ", password=" + password
				+ ", roles=" + roles + "]";
	}

	
}
