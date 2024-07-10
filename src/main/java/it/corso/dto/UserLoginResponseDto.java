package it.corso.dto;

import java.util.Date;

public class UserLoginResponseDto {
	
	private String token;
	private Date ttl;
	private Date tokenCreationTime;
	
	public String getToken() {
		return token;
	}
	
	public Date getTtl() {
		return ttl;
	}
	
	public Date getTokenCreationTime() {
		return tokenCreationTime;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setTtl(Date ttl) {
		this.ttl = ttl;
	}
	
	public void setTokenCreationTime(Date tokenCreationTime) {
		this.tokenCreationTime = tokenCreationTime;
	}

}
