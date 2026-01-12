package org.padre;

import java.time.LocalDate;

public class BibliotecaDTO {
    private Long pkbiblioteca;
    private Long fkusuario;
    private LocalDate fecha;

    public Long getPkbiblioteca() {
        return this.pkbiblioteca;
    }

    public void setPkbiblioteca(Long pkbiblioteca) {
        this.pkbiblioteca = pkbiblioteca;
    }

    public Long getFkusuario() {
        return this.fkusuario;
    }

    public void setFkusuario(Long fkusuario) {
        this.fkusuario = fkusuario;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "BibliotecaDTO{" +
                "pkbiblioteca=" + pkbiblioteca +
                ", fkusuario=" + fkusuario +
                ", fecha=" + fecha +
                '}';
    }
}
