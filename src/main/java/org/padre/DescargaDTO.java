package org.padre;

import java.time.LocalDate;

public class DescargaDTO {
    private Long pkdescarga;
    private Long fkbiblioteca;
    private Long fkjuego;
    private LocalDate fecha;

    public Long getPkdescarga() {
        return this.pkdescarga;
    }

    public void setPkdescarga(Long pkdescarga) {
        this.pkdescarga = pkdescarga;
    }

    public Long getFkbiblioteca() {
        return this.fkbiblioteca;
    }

    public void setFkbiblioteca(Long fkbiblioteca) {
        this.fkbiblioteca = fkbiblioteca;
    }

    public Long getFkjuego() {
        return this.fkjuego;
    }

    public void setFkjuego(Long fkjuego) {
        this.fkjuego = fkjuego;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
