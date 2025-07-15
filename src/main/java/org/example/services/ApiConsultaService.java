package org.example.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class ApiConsultaService {



    static String apiUrl = System.getenv("API_URL");
    static String user = System.getenv("API_USER");
    static String pass = System.getenv("API_PASS");

    public static String consultar(String chave) {

        try {
            if (apiUrl == null || user == null || pass == null) {
                return "Variáveis de ambiente não definidas.";
            }

            URL url = new URL(apiUrl + "/" + chave);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            String basicAuth = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String linha;
            while ((linha = in.readLine()) != null) {
                response.append(linha);
            }
            in.close();
            return response.toString();
        }
        catch (Exception e) {
            return "Erro ao consultar a API: " + e.getMessage();
        }
    }

    private void exibirVar(String apiUrl, String user, String pass) {
        System.out.println(apiUrl);
        System.out.println(user);
        System.out.println(pass);
    }
}
