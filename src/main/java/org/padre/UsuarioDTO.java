package org.padre;

public class UsuarioDTO {
    private Long pkusuario;
    private String alias;
    private String password;
    private String email;
    private Long fkperfil;

    public Long getPkusuario() {
        return this.pkusuario;
    }

    public void setPkusuario(Long pkusuario) {
        this.pkusuario = pkusuario;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getFkperfil() {
        return this.fkperfil;
    }

    public void setFkperfil(Long fkperfil) {
        this.fkperfil = fkperfil;
    }
}
