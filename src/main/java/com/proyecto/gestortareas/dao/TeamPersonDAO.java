package com.proyecto.gestortareas.dao;

import com.proyecto.gestortareas.util.ConexionBD;

import java.sql.*;

public class TeamPersonDAO {

    public void asignarPersonaAEquipo(int idTeam, int idPerson) throws SQLException {
        String sql = "INSERT INTO team_person (id_team, id_person) VALUES (?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTeam);
            ps.setInt(2, idPerson);
            ps.executeUpdate();
        }
    }
}
