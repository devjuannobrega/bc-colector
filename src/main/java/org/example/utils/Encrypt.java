package org.example.utils;

import org.example.models.Usuario;

import java.util.Base64;


public class Encrypt {


    public class CriptografiaBase64 {
        public static String codificar(String texto) {
            return Base64.getEncoder().encodeToString(texto.getBytes());
        }

        public static String decodificar(String textoCodificado) {
            byte[] decoded = Base64.getDecoder().decode(textoCodificado);
            return new String(decoded);
        }
    }

}
