package com.proyecto.gestortareas.modelo;

public class Team {
    private int idTeam;
    private String name;
    private String description;

    public Team() {}

    public Team(int idTeam, String name, String description) {
        this.idTeam = idTeam;
        this.name = name;
        this.description = description;
    }

    public int getIdTeam() { return idTeam; }
    public void setIdTeam(int idTeam) { this.idTeam = idTeam; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() { return name; }
}
