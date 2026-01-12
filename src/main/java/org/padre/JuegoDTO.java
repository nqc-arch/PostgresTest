package org.padre;

public class JuegoDTO {
    private Long pkjuego;
    private String titulo;
    private String urlImagen;
    private Long fkpegi;
    private Long fkdesarrollador;
    private Long numdescargas;

    public Long getPkjuego() {
        return this.pkjuego;
    }

    public void setPkjuego(Long pkjuego) {
        this.pkjuego = pkjuego;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlImagen() {
        return this.urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Long getFkpegi() {
        return this.fkpegi;
    }

    public void setFkpegi(Long fkpegi) {
        this.fkpegi = fkpegi;
    }

    public Long getFkdesarrollador() {
        return this.fkdesarrollador;
    }

    public void setFkdesarrollador(Long fkdesarrollador) {
        this.fkdesarrollador = fkdesarrollador;
    }

    public Long getNumdescargas() {
        return this.numdescargas;
    }

    public void setNumdescargas(Long numdescargas) {
        this.numdescargas = numdescargas;
    }
}
