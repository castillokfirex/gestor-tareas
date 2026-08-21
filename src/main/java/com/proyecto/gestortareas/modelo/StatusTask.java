package com.proyecto.gestortareas.modelo;

public class StatusTask {
    private int idStatusTask;
    private String name;

    public StatusTask() {}

    public StatusTask(int idStatusTask, String name) {
        this.idStatusTask = idStatusTask;
        this.name = name;
    }

    public int getIdStatusTask() { return idStatusTask; }
    public void setIdStatusTask(int idStatusTask) { this.idStatusTask = idStatusTask; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
