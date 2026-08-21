package com.proyecto.gestortareas.modelo;

public class TypePerson {
    private int idTypePerson;
    private String name;

    public TypePerson() {}

    public TypePerson(int idTypePerson, String name) {
        this.idTypePerson = idTypePerson;
        this.name = name;
    }

    public int getIdTypePerson() { return idTypePerson; }
    public void setIdTypePerson(int idTypePerson) { this.idTypePerson = idTypePerson; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
