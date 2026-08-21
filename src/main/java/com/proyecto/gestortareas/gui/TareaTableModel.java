package com.proyecto.gestortareas.gui;

import com.proyecto.gestortareas.modelo.Task;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Modelo de tabla para mostrar tareas en el JTable principal.
 * Mantiene la lista de tareas actual y expone las columnas que
 * queremos ver en pantalla.
 */
public class TareaTableModel extends AbstractTableModel {

    private static final String[] COLUMNAS = {
            "ID", "Titulo", "Prioridad", "Estado", "Equipo", "Asignado a"
    };

    private List<Task> tareas;

    public TareaTableModel(List<Task> tareas) {
        this.tareas = tareas;
    }

    public void actualizar(List<Task> nuevasTareas) {
        this.tareas = nuevasTareas;
        fireTableDataChanged();
    }

    public Task getTareaEn(int fila) {
        return tareas.get(fila);
    }

    @Override
    public int getRowCount() {
        return tareas.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Task t = tareas.get(fila);
        return switch (columna) {
            case 0 -> t.getIdTask();
            case 1 -> t.getTitle();
            case 2 -> t.getAssessment().getName();
            case 3 -> t.getStatus().getName();
            case 4 -> t.getTeam().getName();
            case 5 -> t.getAssignedTo() == null ? "Sin asignar" : t.getAssignedTo().getName();
            default -> "";
        };
    }
}
