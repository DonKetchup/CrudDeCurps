package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    // CREATE - Insertar una persona
    public static boolean insertarPersona(Persona persona) {
        String sql = "INSERT INTO personas (nombre, apellido_paterno, apellido_materno, fecha_nacimiento, entidad_nacimiento, genero, curp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellidoPaterno());
            stmt.setString(3, persona.getApellidoMaterno());
            stmt.setDate(4, java.sql.Date.valueOf(persona.getFechaNac()));
            stmt.setString(5, persona.getEstado());
            stmt.setString(6, persona.getGenero());
            stmt.setString(7, persona.getCurp());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ - Buscar una persona por CURP
    public static Persona buscarPorCURP(String curp) {
        String sql = "SELECT * FROM personas WHERE curp = ?";
        Persona persona = null;

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curp);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                persona = new Persona();
                persona.setNombre(rs.getString("nombre"));
                persona.setApellidoPaterno(rs.getString("apellido_paterno"));
                persona.setApellidoMaterno(rs.getString("apellido_materno"));
                persona.setCurp(rs.getString("curp")); // esto también actualiza fecha, género y estado
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return persona;
    }

    // UPDATE - Actualizar una persona existente
    public static boolean actualizarSoloNombres(Persona persona) {
        String sql = "UPDATE personas SET nombre = ?, apellido_paterno = ?, apellido_materno = ? WHERE curp = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellidoPaterno());
            stmt.setString(3, persona.getApellidoMaterno());
            stmt.setString(4, persona.getCurp());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Eliminar una persona por CURP
    public static boolean eliminarPersonaPorCURP(String curp) {
        String sql = "DELETE FROM personas WHERE curp = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curp);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
