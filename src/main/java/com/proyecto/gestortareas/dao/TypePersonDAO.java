package com.proyecto.gestortareas.dao;

import com.proyecto.gestortareas.modelo.TypePerson;
import com.proyecto.gestortareas.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypePersonDAO {

    public List<TypePerson> listarTodos() throws SQLException {
        String sql = "SELECT id_type_person, name FROM type_person ORDER BY name";
        List<TypePerson> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new TypePerson(rs.getInt("id_type_person"), rs.getString("name")));
            }
        }
        return resultado;
    }
}
