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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="categoria")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID_CA")
	private int id;
	
	@Column(name="Nome_Categoria")
	@Enumerated(EnumType.STRING)
	private NomeCategoria nomeCategoria;
	
	@OneToMany
	(
		cascade = CascadeType.REFRESH,
		fetch = FetchType.EAGER,
		mappedBy = "category",
		orphanRemoval = true		
	)
	private List<Course> courses = new ArrayList<>();
}
