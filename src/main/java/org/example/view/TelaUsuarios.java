package org.example.view;

import org.example.services.UsuarioService;
import org.example.models.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaUsuarios extends JFrame {

    private JList<String> listaUsuarios = new JList<>();

    public TelaUsuarios() {
        setTitle("Gerenciar Usuários");
        setSize(400, 300);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listaUsuarios = new JList<>();
        atualizarLista();

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnVisualizar = new JButton("Visualizar");


        // Adicionar usuário
        btnAdicionar.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Novo usuário:");
            if (nome == null || nome.trim().isEmpty()) return;

            String senha = JOptionPane.showInputDialog(this, "Senha:");
            if (senha == null || senha.trim().isEmpty()) return;

            if (UsuarioService.adicionarUsuario(nome.trim(), senha.trim())) {
                JOptionPane.showMessageDialog(this, "Usuário adicionado com sucesso!");
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar usuário.");
            }
        });

        // Alterar senha
        btnAlterarSenha.addActionListener(e -> {
            String nome = listaUsuarios.getSelectedValue();
            if (nome != null) {
                String novaSenha = JOptionPane.showInputDialog(this, "Nova senha para " + nome + ":");
                if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                    if (UsuarioService.atualizarSenha(nome, novaSenha.trim())) {
                        JOptionPane.showMessageDialog(this, "Senha atualizada com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao atualizar senha.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            }
        });

        //ver credenciais
        btnVisualizar.addActionListener(e -> {
            String nome = listaUsuarios.getSelectedValue();
            if (nome != null) {
                Usuario u = UsuarioService.buscarPorUsuario(nome);
                if (u != null) {
                    String mensagem = String.format(
                            "Usuário: %s\nSenha: %s\nMaster: %s",
                            u.getUsuario(),
                            u.getSenha(),
                            u.isMaster() ? "Sim" : "Não"
                    );
                    JOptionPane.showMessageDialog(this, mensagem, "Credenciais", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        // Excluir usuário
        btnExcluir.addActionListener(e -> {
            String nome = listaUsuarios.getSelectedValue();
            if (nome != null) {
                if (nome.equals("bc-producao")) {
                    JOptionPane.showMessageDialog(this, "Este usuário não pode ser excluído.");
                    return;
                }

                int confirmar = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o usuário " + nome + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirmar == JOptionPane.YES_OPTION) {
                    if (UsuarioService.excluirUsuario(nome)) {
                        JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                        atualizarLista();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir usuário.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            }
        });

        JPanel botoes = new JPanel();
        botoes.add(btnAdicionar);
        botoes.add(btnAlterarSenha);
        botoes.add(btnExcluir);
        botoes.add(btnVisualizar);

        add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void atualizarLista() {
        List<Usuario> usuarios = UsuarioService.listarUsuarios();
        List<String> nomes = usuarios.stream()
                .map(Usuario::getUsuario)
                .toList();
        listaUsuarios.setListData(nomes.toArray(new String[0]));
    }

}
