package org.example.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private int id;
    private String usuario;
    private String senha;
    private boolean isMaster;
            
            //Construtor do usuário
            public Usuario(int id, String usuario, String senha, boolean isMaster) {
                this.id = id;
                this.usuario = usuario;
                this.senha = senha;
                this.isMaster = isMaster;
            }

    //Método que subscreve a função principal
    @Override
    public String toString() {
        return usuario;
    }
}

