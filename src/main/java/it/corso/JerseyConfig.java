package it.corso;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

//jakarta viene da JavaEE ma è implementato dentro spring.
import jakarta.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
	
	// specify what packages jersey should scan
	public JerseyConfig() {
		packages("it.corso");
	}
	
	
	
}
