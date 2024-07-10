package it.corso;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

//jakarta viene da JavaEE ma è implementato dentro spring.
import jakarta.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
	
	//stiamo dicendo a jersey dove cercare il nostro progetto
	public JerseyConfig() {
		packages("it.corso");
	}
	
	
	
}
