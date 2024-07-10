package it.corso.dto;

import it.corso.model.NomeCategoria;

public class CategoryDto {
    private int id;
    private NomeCategoria nomeCategoria;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NomeCategoria getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(NomeCategoria nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}
