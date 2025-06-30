package org.example.view;

import org.example.services.ApiConsultaService;
import org.example.services.ImpressoraService;
import org.example.services.ZplViewer;
import org.example.utils.ApiParser;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TelaPrincipal extends JFrame {

    private JTextField campoChave;
    private JComboBox<String> portaCombo;

    public TelaPrincipal(boolean isMaster) {
        setTitle("BC Collector - Impressão e Consulta de Etiqueta");
        setSize(500, 350);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel label = new JLabel("Chave de acesso:");
        label.setBounds(20, 20, 120, 25);
        add(label);

        campoChave = new JTextField();
        campoChave.setBounds(150, 20, 300, 25);
        add(campoChave);

        JLabel portaLabel = new JLabel("Porta:");
        portaLabel.setBounds(20, 60, 120, 25);
        add(portaLabel);

        portaCombo = new JComboBox<>();
        for (int i = 1; i <= 9; i++) {
            portaCombo.addItem("LPT" + i);
        }
        portaCombo.setBounds(150, 60, 100, 25);
        add(portaCombo);

        JButton imprimirBtn = new JButton("Imprimir Etiqueta");
        imprimirBtn.setBounds(20, 100, 180, 30);
        add(imprimirBtn);

        JButton consultarBtn = new JButton("Consultar Etiqueta");
        consultarBtn.setBounds(210, 100, 180, 30);
        add(consultarBtn);

        imprimirBtn.addActionListener((ActionEvent e) -> {
            String chave = campoChave.getText().trim();
            if (!chave.isEmpty()) {
                String json = ApiConsultaService.consultar(chave);
                String zplUrl = ApiParser.getUrlEtiqueta(json);
                String zpl = ApiParser.baixarZpl(zplUrl);
                String porta = (String) portaCombo.getSelectedItem();
                ImpressoraService.imprimir(porta, zpl);
            } else {
                JOptionPane.showMessageDialog(this, "Informe uma chave válida.");
            }
        });

        consultarBtn.addActionListener((ActionEvent e) -> {
            String chave = campoChave.getText().trim();
            if (!chave.isEmpty()) {
                String json = ApiConsultaService.consultar(chave);
                String urlEtiqueta = ApiParser.getUrlEtiqueta(json);
                ZplViewer.visualizarEtiquetaPorUrl(urlEtiqueta);
            } else {
                JOptionPane.showMessageDialog(this, "Informe uma chave válida.");
            }
        });

        // Se for master, exibe o botão de gerenciamento
        if (isMaster) {
            JButton gerenciarBtn = new JButton("Gerenciar Usuários");
            gerenciarBtn.setBounds(150, 150, 180, 30);
            gerenciarBtn.addActionListener(e -> new TelaUsuarios());
            add(gerenciarBtn);
        }

        setVisible(true);
    }
}
