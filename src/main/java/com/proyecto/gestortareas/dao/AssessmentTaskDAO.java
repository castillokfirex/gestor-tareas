package com.proyecto.gestortareas.dao;

import com.proyecto.gestortareas.modelo.AssessmentTask;
import com.proyecto.gestortareas.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssessmentTaskDAO {

    public List<AssessmentTask> listarTodos() throws SQLException {
        String sql = "SELECT id_assessment_task, name, weight FROM assessment_task ORDER BY weight DESC";
        List<AssessmentTask> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new AssessmentTask(
                        rs.getInt("id_assessment_task"), rs.getString("name"), rs.getInt("weight")));
            }
        }
        return resultado;
    }
}
