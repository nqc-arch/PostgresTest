package org.padre;

import java.sql.*;
import java.time.LocalDate;

public class GestorConsultas {

    String urlDB = "jdbc:postgresql://localhost:5432/postgresql_bd_examen";
    String user = "myuser";
    String password = "mypassword";
    Connection con = null;

    public GestorConsultas() {
        try {
            con = DriverManager.getConnection(urlDB, user, password);
            System.out.println("CONEXION EXITOSA CON POSTGRESQL");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BibliotecaDTO getBibliotecaUsuario(UsuarioDTO usuario) {
        String sql = "select * from biblioteca\n" +
                "inner join usuario on usuario.pkusuario = biblioteca.fkusuario\n" +
                "where usuario.pkusuario = ?;";
        BibliotecaDTO biblioteca;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Math.toIntExact(usuario.getPkusuario()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    biblioteca = new BibliotecaDTO();
                    biblioteca.setPkbiblioteca(Long.valueOf(rs.getInt(1)));
                    biblioteca.setFkusuario(Long.valueOf(rs.getInt(2)));
                    biblioteca.setFecha(rs.getDate(3).toLocalDate());
                } else {
                    System.out.println("ERROR: EL USUARIO NO TIENE UNA BIBLIOTECA ASIGNADA");
                    biblioteca = null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return biblioteca;
    }

    public UsuarioDTO getUsuario(Long pkUsuario) {
        String sql = "select * from usuario where pkusuario = ?";
        UsuarioDTO usuario;
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setLong(1, pkUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new UsuarioDTO();
                    usuario.setPkusuario(rs.getLong("pkusuario"));
                    usuario.setAlias(rs.getString("alias"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setFkperfil(rs.getLong("fkperfil"));
                } else {
                    usuario = null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    public BibliotecaDTO newBiblioteca(UsuarioDTO usuario, Date fecha) {
        if(getUsuario(usuario.getPkusuario()) == null){
            System.out.println("ERROR: NO EXISTE UN USUARIO CON ID " + usuario.getPkusuario());
            return null;
        }
        if (getBibliotecaUsuario(usuario) == null) {
            int pkUsuario = Math.toIntExact(usuario.getPkusuario());
            String sql = "insert into biblioteca (fkusuario,fecha) values\n" +
                    "(?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                //HACEMOS UNA TRANSACCION
                con.setAutoCommit(false);
                ps.setInt(1, pkUsuario);
                ps.setDate(2, fecha);
                int resultado = ps.executeUpdate();
                if (resultado > 0) {
                    System.out.println("BIBLIOTECA CREADA CORRECTAMENTE");
                    //si todo sale bien confirmamos los cambios
                    con.commit();
                } else {
                    //aqui no es necesario el commmit porque no se ha modificado nada
                    System.out.println("ERROR: HA OCURRIDO UN ERROR INESPERADO");
                }
            } catch (SQLException e) {
                //si sale mal hacemos rollback
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("ROLLBACK: HA OCURRIDO UN ERROR INESPERADO");
                }
            } finally {
                try {
                    //VOLVEMOS A ACTIVAR EL AUTOCOMMIT
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //SI SE CREA UNA BIBLIOTECA LA RETORNAMOS CON LA FUNCION
            BibliotecaDTO biblioteca = getBibliotecaUsuario(usuario);
            return biblioteca;
        } else {
            //SINO SE CREA UNA BIBLIOTECA RETORNAMOS NULL
            System.out.println("ERROR: EL USUARIO YA TIENE UNA BIBLIOTECA CREADA.");
            return null;
        }
    }

}
