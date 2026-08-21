package com.proyecto.gestortareas.modelo;

public class Person {
    private int idPerson;
    private String name;
    private String email;
    private TypePerson type;

    public Person() {}

    public Person(int idPerson, String name, String email, TypePerson type) {
        this.idPerson = idPerson;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public int getIdPerson() { return idPerson; }
    public void setIdPerson(int idPerson) { this.idPerson = idPerson; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public TypePerson getType() { return type; }
    public void setType(TypePerson type) { this.type = type; }

    @Override
    public String toString() { return name; }
}
