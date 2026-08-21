package com.proyecto.gestortareas.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private int idTask;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDate dueDate;
    private Team team;
    private Person assignedTo;
    private StatusTask status;
    private AssessmentTask assessment;

    public Task() {}

    public Task(int idTask, String title, String description, Team team,
                StatusTask status, AssessmentTask assessment) {
        this.idTask = idTask;
        this.title = title;
        this.description = description;
        this.team = team;
        this.status = status;
        this.assessment = assessment;
    }

    public int getIdTask() { return idTask; }
    public void setIdTask(int idTask) { this.idTask = idTask; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
    public Person getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Person assignedTo) { this.assignedTo = assignedTo; }
    public StatusTask getStatus() { return status; }
    public void setStatus(StatusTask status) { this.status = status; }
    public AssessmentTask getAssessment() { return assessment; }
    public void setAssessment(AssessmentTask assessment) { this.assessment = assessment; }

    @Override
    public String toString() {
        String responsable = (assignedTo == null) ? "Sin asignar" : assignedTo.getName();
        return String.format("#%-3d | %-28s | Prioridad: %-6s | Estado: %-11s | Asignado a: %s",
                idTask, title, assessment.getName(), status.getName(), responsable);
    }
}
