package com.proyecto.gestortareas.dao;

import com.proyecto.gestortareas.modelo.StatusTask;
import com.proyecto.gestortareas.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusTaskDAO {

    public List<StatusTask> listarTodos() throws SQLException {
        String sql = "SELECT id_status_task, name FROM status_task ORDER BY id_status_task";
        List<StatusTask> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new StatusTask(rs.getInt("id_status_task"), rs.getString("name")));
            }
        }
        return resultado;
    }
}
