package org.example.services;

import org.example.data.Banco;
import org.example.models.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    public static boolean autenticar(String usuario, String senha) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ?";
        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // retorna true se encontrou
        } catch (Exception e) {
            System.out.println("Erro ao autenticar: " + e.getMessage());
            return false;
        }
    }

    public static Usuario buscarPorId(int id) {
        try (Connection conn = Banco.conectar()) {
            String sql = "SELECT * FROM usuarios WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsuario(rs.getString("usuario"));
                u.setSenha(rs.getString("senha"));
                u.setMaster(rs.getBoolean("is_master"));
                return u;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }

    public static boolean atualizarUsuario(int id, String usuario, String senha) {
        try (Connection conn = Banco.conectar()) {
            String sql = "UPDATE usuarios SET usuario = ?, senha = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    public static boolean isMaster(String nome, String senha) {
        return nome.equals("bc-producao") && senha.equals("Bcf123");
    }

    public static List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = Banco.conectar()) {
            String sql = "SELECT id, usuario, senha, is_master FROM usuarios";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setUsuario(rs.getString("usuario"));
                u.setSenha(rs.getString("senha"));
                u.setMaster(rs.getBoolean("is_master"));
                lista.add(u);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
        return lista;
    }


    public static boolean adicionarUsuario(String nome, String senha) {
        try (Connection conn = Banco.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO usuarios (usuario, senha, is_master) VALUES (?, ?, ?)"
            );
            stmt.setString(1, nome);
            stmt.setString(2, senha);
            stmt.setBoolean(3, false); // ou true, se for usuário master
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao adicionar usuário: " + e.getMessage());
            return false;
        }
    }


    public static boolean atualizarSenha(String nome, String novaSenha) {
        try (Connection conn = Banco.conectar()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET senha = ? WHERE nome = ?");
            stmt.setString(1, novaSenha);
            stmt.setString(2, nome);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean excluirUsuario(String nome) {
        try (Connection conn = Banco.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM usuarios WHERE usuario = ?"
            );
            stmt.setString(1, nome);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
            return false;
        }
    }

    public static Usuario buscarPorUsuario(String nome) {
        try (Connection conn = Banco.conectar()) {
            String sql = "SELECT * FROM usuarios WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("senha"),
                        rs.getBoolean("is_master")
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }
}
