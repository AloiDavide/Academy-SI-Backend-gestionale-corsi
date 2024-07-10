package it.corso.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;


//inserimento ruolo a mano, dopo che inserisci un utente

@Entity
@Table(name = "ruolo")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_G")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPOLOGIA")
    private Tipologia tipologia;
    
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable
	(
		name = "utente_ruolo",
		joinColumns = @JoinColumn(name="FK_R", referencedColumnName = "ID_G"),
		inverseJoinColumns = @JoinColumn(name="FK_U", referencedColumnName = "ID_U")
		
	)
	private List<User> users = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia type) {
		this.tipologia = type;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> roles) {
		this.users = roles;
	}


}
