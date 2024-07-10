package it.corso.dto;

import java.util.List;

import it.corso.model.Typology;

public class RoleDto {
    private int id;
    private Typology typology;

    public int getId() {
        return id;
    }

    public Typology getTypology() {
		return typology;
	}

	public void setTypology(Typology typology) {
		this.typology = typology;
	}

	public void setId(int id) {
        this.id = id;
    }

}
