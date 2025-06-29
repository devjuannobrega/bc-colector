package org.example.services;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZplViewer {

    public static void visualizarEtiquetaPorUrl(String urlTxtZpl) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    String zpl = baixarZpl(urlTxtZpl);

                    if (zpl == null || zpl.isEmpty()) {
                        mostrarMensagem("Etiqueta vazia ou falha ao baixar.");
                        return null;
                    }

                    URL url = new URL("http://api.labelary.com/v1/printers/8dpmm/labels/4x6/0/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    OutputStream os = conn.getOutputStream();
                    os.write(zpl.getBytes());
                    os.flush();
                    os.close();

                    BufferedImage image = ImageIO.read(conn.getInputStream());

                    if (image != null) {
                        JLabel label = new JLabel(new ImageIcon(image));
                        JOptionPane.showMessageDialog(null, label, "Visualização da Etiqueta", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        mostrarMensagem("Erro ao renderizar a etiqueta.");
                    }

                } catch (Exception e) {
                    mostrarMensagem("Erro ao exibir etiqueta: " + e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    private static String baixarZpl(String urlStr) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlStr).openStream()))) {
            StringBuilder conteudo = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                conteudo.append(linha).append("\n");
            }
            return conteudo.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private static void mostrarMensagem(String msg) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, msg)
        );
    }
}
