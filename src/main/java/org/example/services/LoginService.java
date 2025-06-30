package org.example.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.example.data.Banco;

public class LoginService {

    public static boolean autenticar(String usuario, String senha) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nome = ? AND senha = ?";

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.out.println("Erro ao autenticar: " + e.getMessage());
        }

        return false;
    }
}

