package it.corso.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.User;

public interface UserDao extends CrudRepository<User, Integer>{
	
	//Query methods automatici.
	boolean existsByMail(String email);
	Optional<User> findByMail(String email);
	Optional<User> findByMailAndPassword(String email, String password);
}
