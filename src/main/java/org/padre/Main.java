package org.padre;

import java.sql.Date;
import java.sql.SQLOutput;
import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.println("=======INICIAR GESTOR CONSULTAS=========");
        GestorConsultas gestorConsultas = new GestorConsultas();
        System.out.println("===================RETORNA LA BIBLIOTECA DE UN USUARIO=================");
        UsuarioDTO testUsuario = new UsuarioDTO();
        testUsuario.setPkusuario(4L);
        BibliotecaDTO b =  gestorConsultas.getBibliotecaUsuario(testUsuario);
        if(b!=null){
            System.out.println(b.toString());
        }
        System.out.println("===================CREACION DE UNA BIBLIOTECA============");
        b = gestorConsultas.newBiblioteca(testUsuario, Date.valueOf(LocalDate.now()));
        if(b!=null){
            System.out.println(b.toString());
        }
    }
}