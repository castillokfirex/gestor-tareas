package com.proyecto.gestortareas.dao;

import com.proyecto.gestortareas.modelo.*;
import com.proyecto.gestortareas.util.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de tareas. Aqui se ve de forma explicita la asociacion entre "task" y
 * "status_task", "assessment_task", "team" y "person": cada JOIN del SELECT
 * es una de esas relaciones del modelo E-R.
 */
public class TaskDAO {

    private static final String SELECT_BASE =
        "SELECT t.id_task, t.title, t.description, t.created_at, t.due_date, " +
        "       tm.id_team, tm.name AS team_name, tm.description AS team_desc, " +
        "       st.id_status_task, st.name AS status_name, " +
        "       at.id_assessment_task, at.name AS assessment_name, at.weight, " +
        "       p.id_person, p.name AS person_name, p.email AS person_email, " +
        "       tp.id_type_person, tp.name AS type_name " +
        "FROM task t " +
        "JOIN team tm            ON t.id_team = tm.id_team " +
        "JOIN status_task st      ON t.id_status_task = st.id_status_task " +
        "JOIN assessment_task at  ON t.id_assessment_task = at.id_assessment_task " +
        "LEFT JOIN person p       ON t.id_person = p.id_person " +
        "LEFT JOIN type_person tp ON p.id_type_person = tp.id_type_person";

    public List<Task> listarTodas() throws SQLException {
        String sql = SELECT_BASE + " ORDER BY t.id_task";
        return ejecutarListado(sql, ps -> {});
    }

    /** Tareas de una persona, ordenadas de mayor a menor prioridad. */
    public List<Task> listarPorPersona(int idPersona) throws SQLException {
        String sql = SELECT_BASE + " WHERE p.id_person = ? ORDER BY at.weight DESC";
        return ejecutarListado(sql, ps -> ps.setInt(1, idPersona));
    }

    /** Tareas agrupadas por estado (tablero tipo Kanban). */
    public List<Task> listarPorEstado(int idStatusTask) throws SQLException {
        String sql = SELECT_BASE + " WHERE st.id_status_task = ? ORDER BY at.weight DESC";
        return ejecutarListado(sql, ps -> ps.setInt(1, idStatusTask));
    }

    public Task crear(String titulo, String descripcion, int idTeam,
                       int idStatusTask, int idAssessmentTask) throws SQLException {
        String sql = "INSERT INTO task (title, description, id_team, id_status_task, id_assessment_task) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, titulo);
            ps.setString(2, descripcion);
            ps.setInt(3, idTeam);
            ps.setInt(4, idStatusTask);
            ps.setInt(5, idAssessmentTask);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return buscarPorId(rs.getInt(1));
            }
        }
    }

    public void asignarPersona(int idTask, int idPersona) throws SQLException {
        String sql = "UPDATE task SET id_person = ? WHERE id_task = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            ps.setInt(2, idTask);
            ps.executeUpdate();
        }
    }

    public void cambiarEstado(int idTask, int idStatusTask) throws SQLException {
        String sql = "UPDATE task SET id_status_task = ? WHERE id_task = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idStatusTask);
            ps.setInt(2, idTask);
            ps.executeUpdate();
        }
    }

    public Task buscarPorId(int idTask) throws SQLException {
        String sql = SELECT_BASE + " WHERE t.id_task = ?";
        List<Task> resultado = ejecutarListado(sql, ps -> ps.setInt(1, idTask));
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    // ---------------------------------------------------------------
    // Ayudante interno para no repetir el codigo de mapeo ResultSet -> Task
    // ---------------------------------------------------------------

    private interface Parametrizador {
        void aplicar(PreparedStatement ps) throws SQLException;
    }

    private List<Task> ejecutarListado(String sql, Parametrizador parametrizador) throws SQLException {
        List<Task> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            parametrizador.aplicar(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        }
        return resultado;
    }

    private Task mapear(ResultSet rs) throws SQLException {
        Team equipo = new Team(rs.getInt("id_team"), rs.getString("team_name"), rs.getString("team_desc"));
        StatusTask estado = new StatusTask(rs.getInt("id_status_task"), rs.getString("status_name"));
        AssessmentTask prioridad = new AssessmentTask(
                rs.getInt("id_assessment_task"), rs.getString("assessment_name"), rs.getInt("weight"));

        Task tarea = new Task(rs.getInt("id_task"), rs.getString("title"), rs.getString("description"),
                equipo, estado, prioridad);

        Timestamp creado = rs.getTimestamp("created_at");
        if (creado != null) tarea.setCreatedAt(creado.toLocalDateTime());
        Date vence = rs.getDate("due_date");
        if (vence != null) tarea.setDueDate(vence.toLocalDate());

        int idPersona = rs.getInt("id_person");
        if (!rs.wasNull()) {
            TypePerson tipo = new TypePerson(rs.getInt("id_type_person"), rs.getString("type_name"));
            Person persona = new Person(idPersona, rs.getString("person_name"), rs.getString("person_email"), tipo);
            tarea.setAssignedTo(persona);
        }
        return tarea;
    }
}
