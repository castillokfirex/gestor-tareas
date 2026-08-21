package com.proyecto.gestortareas.modelo;

import java.time.LocalDate;

public class TeamPerson {
    private int idTeamPerson;
    private Team team;
    private Person person;
    private LocalDate joinedAt;

    public TeamPerson() {}

    public TeamPerson(int idTeamPerson, Team team, Person person, LocalDate joinedAt) {
        this.idTeamPerson = idTeamPerson;
        this.team = team;
        this.person = person;
        this.joinedAt = joinedAt;
    }

    public int getIdTeamPerson() { return idTeamPerson; }
    public void setIdTeamPerson(int idTeamPerson) { this.idTeamPerson = idTeamPerson; }
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }
    public LocalDate getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDate joinedAt) { this.joinedAt = joinedAt; }
}
