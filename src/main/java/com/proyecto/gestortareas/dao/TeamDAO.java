package com.proyecto.gestortareas.dao;

import com.proyecto.gestortareas.modelo.Team;
import com.proyecto.gestortareas.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    public List<Team> listarTodos() throws SQLException {
        String sql = "SELECT id_team, name, description FROM team ORDER BY name";
        List<Team> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Team(rs.getInt("id_team"), rs.getString("name"), rs.getString("description")));
            }
        }
        return resultado;
    }

    public Team crear(String nombre, String descripcion) throws SQLException {
        String sql = "INSERT INTO team (name, description) VALUES (?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return new Team(rs.getInt(1), nombre, descripcion);
            }
        }
    }
}
