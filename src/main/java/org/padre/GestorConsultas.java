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

    /**
     * Retorna la biblioteca del usuarioDTO pasado como parametro
     *
     * @param usuario
     * @return
     */
    public BibliotecaDTO getBibliotecaUsuario(UsuarioDTO usuario) {
        String sql = "select * from biblioteca\n" + "inner join usuario on usuario.pkusuario = biblioteca.fkusuario\n" + "where usuario.pkusuario = ?;";
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

    /**
     * Retorna el usuario cuya pk es pasada como parametro
     *
     * @param pkUsuario
     * @return
     */
    public UsuarioDTO getUsuario(Long pkUsuario) {
        String sql = "select * from usuario where pkusuario = ?";
        UsuarioDTO usuario;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
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

    /**
     * Crea un objeto biblioteca para un usuario siempre que este ya no disponga de una biblioteca
     * ya creada. Solo puede haber una biblioteca por usuario.
     *
     * @param usuario
     * @param fecha
     * @return
     */
    public BibliotecaDTO newBiblioteca(UsuarioDTO usuario, Date fecha) {
        if (getUsuario(usuario.getPkusuario()) == null) {
            System.out.println("ERROR: NO EXISTE UN USUARIO CON ID " + usuario.getPkusuario());
            return null;
        }
        if (getBibliotecaUsuario(usuario) == null) {
            int pkUsuario = Math.toIntExact(usuario.getPkusuario());
            String sql = "insert into biblioteca (fkusuario,fecha) values\n" + "(?,?)";
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

    /**
     * CRUD FOR Pegi table
     **/

    /**
     * CREA UN NUEVO PEGI EN LA BASE DE DATOS
     **/

    public void newPegi(PegiDTO pegi) {
        if (pegi == null) {
            System.out.println("ERROR: NO HAY DATOS EN EL OBJETO PEGI PARAMETRO");
        } else {
            String sql = "insert into pegi (rangopegi,observaciones) values\n" + "(?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                //HACEMOS UNA TRANSACCION
                con.setAutoCommit(false);
                ps.setString(1, pegi.getRangopegi());
                ps.setString(2, pegi.getObservaciones());
                int resultado = ps.executeUpdate();
                if (resultado > 0) {
                    System.out.println("PEGI CREADO CORRECTAMENTE");
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
                    System.out.println("");
                }
            }
        }
    }

    /**
     * Borra un registro de PEGI cuya id es dada como parametro
     *
     * @param pegiId
     */
    public void deletePegiById(int pegiId) {
        String sql = "delete from pegi  where pkpegi = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pegiId);
            //Iniciamos transaccion
            con.setAutoCommit(false);
            if (ps.executeUpdate() > 0) {
                System.out.println("REGISTRO DE PEGI BORRADO CORRECTAMENTE");
                con.commit();
            } else {
                System.out.println("NO SE ENCONTRO NINGUN REGISTRO PEGI CON ESE ID");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: HA OCURRIDO UN ERROR INESPERADO");
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("");
            }
        }
    }


    /**
     * Retorna un registro pegi cuyo id es dado como parametro
     *
     * @param pegiId
     * @return
     */
    public PegiDTO getPegiById(int pegiId) {
        String sql = "select * from pegi  where pkpegi = ?";
        PegiDTO pegi = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pegiId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pegi = new PegiDTO();
                    pegi.setPkpegi(rs.getLong("pkpegi"));
                    pegi.setRangopegi(rs.getString("rangopegi"));
                    pegi.setObservaciones(rs.getString("observaciones"));
                } else {
                    System.out.println("ERR0R: NO SE ENCONTR NINGN REGISTRO PEGI CON ESE ID");
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: OCURRIO UN ERROR INESPERADO.");
        }
        return pegi;
    }

    /**
     * Aumenta las descargas de un juego qen uno, verificando primero si el usuario
     * existe y luego si tiene una biblioteca asignada. Si no tiene ninguna biblioteca registrada, creara una. Si el
     * usuario no existe devuelve false
     *
     * @param pkUsuario
     * @param pkJuego
     * @return
     */
    public boolean descargarJuego(Long pkUsuario , int pkJuego) {
        UsuarioDTO usuario = getUsuario(pkUsuario);
        if (usuario == null) {
            System.out.println("ERROR: EL USUARIO NO EXISTE. DEBE REGISTRARSE ANTES DE DESCARGAR UN JUEGO");
            return false;
        }
        if(getBibliotecaUsuario(usuario) == null){
            System.out.println("CREANDO NUEVA BIBLIOTECA....");
            newBiblioteca(usuario , Date.valueOf(LocalDate.now()));
            System.out.println("BIBLIOTECA CREADA CORRECTAMENTE");
        }
        aumentarDescargasJuego(pkJuego);
        System.out.println("JUEGO DESCARGADO EXITOSAMENTE");
        return true;
    }

    /**
     * Aumenta el numero de descargas del juego cuya primary key se pasa como
     * parametro en 1
     *
     * @param pkJuego
     * @return
     */
    public boolean aumentarDescargasJuego(int pkJuego) {
        String sql = "update juego\n" +
                "set numDescargas = \n" +
                "(select numDescargas from juego where pkjuego = ?) + 1\n" +
                "where pkjuego = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            //establecemos ambos parametros del PreparedStatement como la primary key del juego
            //que vamos a actualizar
            ps.setInt(1, pkJuego);
            ps.setInt(2, pkJuego);
            con.setAutoCommit(false);
            if (ps.executeUpdate() > 0) {
                con.commit();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("ERROR: OCURRIO UN ERROR INESPERADO");
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("");
            }
        }
    }
}




