package it.corso.jwt;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@JWTTokenNeeded
@Provider
public class JWTTokenNeededFilter implements ContainerRequestFilter {
    
	@Context
	private ResourceInfo resourceInfo;
  
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Secured annotatedRole = resourceInfo.getResourceMethod().getAnnotation(Secured.class); 
  
		if (annotatedRole == null) {
			// no security on method: check the class
			annotatedRole =  resourceInfo.getResourceClass().getAnnotation(Secured.class);
		}
  
		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
  
		// Check if the HTTP Authorization header is present and formatted correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
  
		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim(); 
  
		try {
  
			// Validate the token
			byte[] secret = "supersecretpassword1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".getBytes();
			Key key = Keys.hmacShaKeyFor(secret);  
    
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey (key).build().parseClaimsJws (token); 
			Claims body = claims.getBody();
			List<String> rolesToken = body.get("ruoli", List.class);
    
			Boolean hasRole = false;
			
			for (String role: rolesToken) {
				if (role.equals (annotatedRole.role())) {
					hasRole=true;
				}
			}
      
			if (!hasRole) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}
}
