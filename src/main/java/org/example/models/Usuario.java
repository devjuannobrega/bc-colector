package org.example.models;

public class Usuario {
    private int id;
    private String usuario;
    private String senha;
    private boolean isMaster;

    public Usuario(int id, String usuario, String senha, boolean isMaster) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
        this.isMaster = isMaster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    @Override
    public String toString() {
        return usuario;
    }
}

