package org.example.view;

import org.example.services.UsuarioService;
import org.example.services.LoginService;
import java.lang.String.*;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoSenha;

    public TelaLogin() {
        setTitle("Login - BC Collector");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        campoUsuario = new JTextField();
        campoSenha = new JPasswordField();
        JButton botaoEntrar = new JButton("Entrar");

        add(new JLabel("Usuário:"));
        add(campoUsuario);
        add(new JLabel("Senha:"));
        add(campoSenha);
        add(botaoEntrar);

        botaoEntrar.addActionListener(e -> autenticar());

        setVisible(true);
    }

    private void autenticar() {
        String nome = campoUsuario.getText();
        String senha = new String(campoSenha.getPassword());

        // Aqui valida direto no banco MySQL via LoginService
        boolean isAutenticado = LoginService.autenticar(nome, senha);
        boolean isMaster = false;

        if (isAutenticado) {
            isMaster = UsuarioService.isMaster(nome,senha);
            dispose(); // Fecha a tela de login
            new TelaPrincipal(isMaster); // Passa true se for master
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos");
        }
    }
}
