package it.corso.dto;

import it.corso.model.Tipologia; // Adjust the import according to your package structure

public class RoleDto {
    private int id;
    private Tipologia tipologia;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }
}
