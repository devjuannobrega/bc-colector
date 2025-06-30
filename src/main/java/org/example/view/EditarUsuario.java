package org.example.view;

import org.example.services.UsuarioService;
import org.example.models.Usuario;
import javax.swing.*;
import java.awt.*;

public class EditarUsuario extends JFrame {
    private final JTextField campoUsuario;
    private final JPasswordField campoSenha;
    private final int userId;

    public EditarUsuario(int id) {
        this.userId = id;

        Usuario user = UsuarioService.buscarPorId(id);
        setTitle("Editar Usuário");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        campoUsuario = new JTextField(user.getUsuario());
        campoSenha = new JPasswordField(user.getSenha());

        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> salvarEdicao());

        add(new JLabel("Usuário:"));
        add(campoUsuario);
        add(new JLabel("Senha:"));
        add(campoSenha);
        add(new JLabel(""));
        add(salvar);

        setVisible(true);
    }

    private void salvarEdicao() {
        String novoUsuario = campoUsuario.getText();
        String novaSenha = new String(campoSenha.getPassword());
        UsuarioService.atualizarUsuario(userId, novoUsuario, novaSenha);
        JOptionPane.showMessageDialog(this, "Usuário atualizado!");
        dispose();
    }
}
