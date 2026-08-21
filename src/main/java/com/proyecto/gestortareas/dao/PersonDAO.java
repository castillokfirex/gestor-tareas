package com.proyecto.gestortareas.dao;

import com.proyecto.gestortareas.modelo.Person;
import com.proyecto.gestortareas.modelo.TypePerson;
import com.proyecto.gestortareas.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    private static final String SELECT_BASE =
            "SELECT p.id_person, p.name, p.email, tp.id_type_person, tp.name AS type_name " +
            "FROM person p JOIN type_person tp ON p.id_type_person = tp.id_type_person";

    public List<Person> listarTodos() throws SQLException {
        String sql = SELECT_BASE + " ORDER BY p.name";
        List<Person> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        }
        return resultado;
    }

    public Person crear(String nombre, String email, int idTypePerson) throws SQLException {
        String sql = "INSERT INTO person (name, email, id_type_person) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setInt(3, idTypePerson);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                int nuevoId = rs.getInt(1);
                return buscarPorId(nuevoId);
            }
        }
    }

    public Person buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE p.id_person = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Person mapear(ResultSet rs) throws SQLException {
        TypePerson tipo = new TypePerson(rs.getInt("id_type_person"), rs.getString("type_name"));
        return new Person(rs.getInt("id_person"), rs.getString("name"), rs.getString("email"), tipo);
    }
}
