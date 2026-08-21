package com.proyecto.gestortareas.servicio;

import com.proyecto.gestortareas.dao.TaskDAO;
import com.proyecto.gestortareas.dao.TeamPersonDAO;
import com.proyecto.gestortareas.modelo.Task;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Capa de servicio: coordina el DAO de tareas con la logica de negocio.
 *
 * Aqui vive la COLA (java.util.Queue) del sistema: cada vez que se crea una
 * tarea nueva, entra a una cola de revision en memoria. Un lider de equipo
 * puede ir sacando tareas de la cola en el mismo orden en que se crearon
 * (FIFO) para revisarlas antes de darlas por aprobadas. Esto es justamente
 * el caso de uso donde SI tiene sentido una cola: procesar en orden de
 * llegada, sin necesidad de buscar por ID ni ordenar por prioridad (eso ya
 * lo hace la lista que trae TaskDAO).
 */
public class TaskService {

    private final TaskDAO taskDAO = new TaskDAO();
    private final TeamPersonDAO teamPersonDAO = new TeamPersonDAO();

    private final Queue<Task> colaDeRevision = new LinkedList<>();

    public Task crearTarea(String titulo, String descripcion, int idTeam,
                            int idStatusTask, int idAssessmentTask) throws SQLException {
        Task nueva = taskDAO.crear(titulo, descripcion, idTeam, idStatusTask, idAssessmentTask);
        colaDeRevision.offer(nueva);
        return nueva;
    }

    /** Saca (y elimina) la siguiente tarea a revisar, en orden FIFO. */
    public Task siguienteEnRevision() {
        return colaDeRevision.poll();
    }

    /** Mira cual es la siguiente sin sacarla de la cola. */
    public Task verSiguienteEnRevision() {
        return colaDeRevision.peek();
    }

    public int tamanoColaRevision() {
        return colaDeRevision.size();
    }

    public List<Task> listarTodas() throws SQLException {
        return taskDAO.listarTodas();
    }

    public List<Task> listarPorPersona(int idPersona) throws SQLException {
        return taskDAO.listarPorPersona(idPersona);
    }

    public List<Task> listarPorEstado(int idStatusTask) throws SQLException {
        return taskDAO.listarPorEstado(idStatusTask);
    }

    public void asignarPersona(int idTask, int idPersona) throws SQLException {
        taskDAO.asignarPersona(idTask, idPersona);
    }

    public void cambiarEstado(int idTask, int idStatusTask) throws SQLException {
        taskDAO.cambiarEstado(idTask, idStatusTask);
    }

    public void asignarPersonaAEquipo(int idTeam, int idPersona) throws SQLException {
        teamPersonDAO.asignarPersonaAEquipo(idTeam, idPersona);
    }
}
