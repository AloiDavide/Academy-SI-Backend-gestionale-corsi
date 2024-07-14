package it.corso.controller;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.dto.UserDto;
import it.corso.dto.UserLoginRequestDto;
import it.corso.dto.UserLoginResponseDto;
import it.corso.dto.UserSignupDto;
import it.corso.dto.UserUpdateDto;
import it.corso.exceptions.ObjectNotFoundException;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.jwt.Secured;
import it.corso.model.Role;
import it.corso.model.User;
import it.corso.service.CourseService;
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

@RestController
@Path("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	private final String validPassword = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}";

	
	
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
		//encrypt a very long string
		byte[] secret = "supersecretpassword1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".getBytes();
		Key key = Keys.hmacShaKeyFor(secret);
		
		//use a HashMap to make a dictionary of the user's data
		User userInfo = null;
		try {
			userInfo = userService.getUserByMail(email);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("nome", userInfo.getName());
		map.put("cognome", userInfo.getLastname());
		map.put("email", email);
		
		List<String> roles = new ArrayList<>();
		for (Role role: userInfo.getRoles()) {
			roles.add(role.getTypology().name());
		}
		
		map.put("ruoli", roles);
		
		// find creation and expiration time
		Date creation = new Date();
		Date end = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));
		
		// build the token using Jwts.builder() and the data gathered so far
		String jwtToken = Jwts.builder()
		                        .setClaims (map)
		                        // setIssuer is the emitter of the token
		                        .setIssuer ("http://localhost:8080") 
		                        .setIssuedAt (creation)
		                        .setExpiration (end)
		                        // the token is signed with the encrypted key, if the client or an attacker modify it, the token won't be valid anymore
		                        .signWith(key)
		                        // compact the token into a string
		                        .compact();
		
		
		//create and return the response dto with the token
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
	public Response userRegistration(@Valid @RequestBody UserSignupDto userSignupDto) {
		System.out.println("\n\n CONTROLLER:\n"+ userSignupDto.toString()+"\n\n\n");
		try {
	    	//controllo validità password
	        if (!Pattern.matches(this.validPassword, userSignupDto.getPassword())) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }
	        //controllo che l'utente non sia già registrato
	        if (userService.existsUserByEmail(userSignupDto.getMail())) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }

	        userService.userSignup(userSignupDto);
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
	    	//controllo validità password
	        if (!Pattern.matches(this.validPassword, userUpdate.getPassword())) {
	            return Response.status(Response.Status.BAD_REQUEST).build();
	        }

	        userService.updateUserData(userUpdate);
	        return Response.status(Response.Status.OK).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}
	
	@PUT
	@Path("/{email}/subscribe/{courseId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response subscribeToCourse(@PathParam("email") String email, @PathParam("courseId") int courseId) {
		try {
			userService.subscribeToCourse(email, courseId);
			return Response.status(Response.Status.OK).build();
		} catch (NoSuchElementException e) {
	        return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	    }
	}
	
	
	@PUT
	@Path("/{email}/unsubscribe/{courseId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response unsubscribeFromCourse(@PathParam("email") String email, @PathParam("courseId") int courseId) {
		try {
			userService.unsubscribeFromCourse(email, courseId);
			return Response.status(Response.Status.OK).build();
			
		} catch (NoSuchElementException e) {
	        return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
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
	    	e.printStackTrace();
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
	@Path("/delete/{email}")
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

