package com.proyecto.gestortareas.modelo;

public class AssessmentTask {
    private int idAssessmentTask;
    private String name;
    private int weight;

    public AssessmentTask() {}

    public AssessmentTask(int idAssessmentTask, String name, int weight) {
        this.idAssessmentTask = idAssessmentTask;
        this.name = name;
        this.weight = weight;
    }

    public int getIdAssessmentTask() { return idAssessmentTask; }
    public void setIdAssessmentTask(int idAssessmentTask) { this.idAssessmentTask = idAssessmentTask; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    @Override
    public String toString() { return name; }
}
