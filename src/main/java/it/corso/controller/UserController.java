package it.corso.controller;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.dto.UserDto;
import it.corso.dto.UserLoginRequestDto;
import it.corso.dto.UserLoginResponseDto;
import it.corso.dto.UserSignupDto;
import it.corso.dto.UserUpdateDto;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.model.Role;
import it.corso.model.User;
import it.corso.service.UserService;
import jakarta.validation.Valid;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/user")
public class UserController {
	@Autowired
	private UserService userService;
	

	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
		try {
			if (userService.login(userLoginRequestDto)) {
				//il metodo ok() prende a parametro il token creato da issueToken() e lo include nella risposta
				return Response.ok(issueToken(userLoginRequestDto.getEmail())).build();
			}
			
		} 
		catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	
	
	
	private UserLoginResponseDto issueToken(String email) {
		
		byte[] secret = "supersecretpassword1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".getBytes();
		Key key = Keys.hmacShaKeyFor(secret);
		
		//use a map to make a dictionary of the user's data
		User userInfo = userService.getUserByMail(email);
		Map<String, Object> map = new HashMap<>();
		map.put("nome", userInfo.getName());
		map.put("cognome", userInfo.getLastname());
		map.put("email", email);
		
		List<String> ruoli = new ArrayList<>();
		for (Role role: userInfo.getRoles()) {
		    ruoli.add(role.getTipologia().name());
		}
		
		map.put("roles", ruoli);
		
		Date creation = new Date();
		Date end = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));
		String jwtToken = Jwts.builder()
		                        .setClaims (map)
		                        // setIssuer chi emette il token
		                        .setIssuer ("http://localhost:8080") 
		                        .setIssuedAt (creation)
		                        .setExpiration (end)
		                        // firma il token JWT utilizzando la chiave segreta 
		                        .signWith(key)
		                        // compatta il token in una stringa codifica 
		                        .compact();
		
		UserLoginResponseDto token = new UserLoginResponseDto(); 
		  
		token.setToken (jwtToken);
		token.setTtl (end);
		token.setTokenCreationTime (creation);
		  
		return token;

		
	}
	
	
	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userRegistration(@Valid @RequestBody UserSignupDto userSignup) {
	    try {
	        if (!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}", userSignup.getPassword())) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }

	        if (userService.existsUserByEmail(userSignup.getEmail())) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }

	        userService.userSignup(userSignup);
	        return Response.status(Response.Status.OK).build();
	    } catch (Exception e) {
	        // alternatively server error
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}

	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userUpdate(@Valid @RequestBody UserUpdateDto userUpdate) {
	    try {
	        if (!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}", userUpdate.getPassword())) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }

	        userService.updateUserData(userUpdate);
	        return Response.status(Response.Status.OK).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}

	@GET
	@Path("/get/{email}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByEmail(@PathParam("email") String email) {
	    try {
	        UserDto userDto = userService.getUserDtoByMail(email);
	        return Response.status(Response.Status.OK)
	                .entity(userDto)
	                .build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}

	@GET
	@Path("/get/all")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
	    try {
	        List<UserDto> usersList = userService.getUsers();
	        return Response.status(Response.Status.OK).entity(usersList).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}

	@DELETE
	@Path("/delete/{email}") // this should be based on email
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@Valid @PathParam("email") String email) {
	    try {
	        userService.deleteUser(email);
	        return Response.status(Response.Status.OK).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}
	

	
}

