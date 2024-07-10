package it.corso.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name="utente")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID_U")
	private int id;
	
	@Pattern(regexp = "[a-zA-Z\\èàùìò]{1,50}", message = "nome con caratteri non ammessi")
	@Column(name="Nome")
    private String name;
	
	@Pattern(regexp = "[a-zA-Z\\èàùìò]{1,50}", message = "cognome con caratteri non ammessi")
	@Column(name="Cognome")
    private String lastname;
	
	@Pattern(regexp = "[A-z0-9\\.\\+_-]+@[A-z0-9\\._-]+\\.[A-z]{2,8}", message = "mail non valida")
	@Column(name="email")
    private String mail;
	

	@Column(name="password")
    private String password;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable
	(
		name = "utenti_corsi",
		joinColumns = @JoinColumn(name="FK_UC", referencedColumnName = "ID_U"),
		inverseJoinColumns = @JoinColumn(name="FK_CU", referencedColumnName = "ID_C")
		
	)
	private List<Course> courses = new ArrayList<>();
	
	
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable
	(
		name = "utente_ruolo",
		joinColumns = @JoinColumn(name="FK_U", referencedColumnName = "ID_U"),
		inverseJoinColumns = @JoinColumn(name="FK_R", referencedColumnName = "ID_G")
		
	)
	private List<Role> roles = new ArrayList<>();
	
		

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> corsi) {
		this.courses = corsi;
	}
	

}
