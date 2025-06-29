package org.example.services;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImpressoraService {

    public static void imprimir(String porta, String conteudoZpl) {
        try (OutputStream os = new FileOutputStream(porta)) {
            os.write(conteudoZpl.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
