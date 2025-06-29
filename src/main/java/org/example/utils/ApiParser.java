package org.example.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ApiParser {

    public static String getUrlEtiqueta(String json) {
        try {
            JSONArray array = new JSONArray(json);
            JSONObject pedido = array.getJSONObject(0);
            JSONObject entrega = pedido.getJSONArray("entregas").getJSONObject(0);
            return entrega.getString("etiqueta");
        } catch (Exception e) {
            return null;
        }
    }

    public static String baixarZpl(String urlStr) {
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


    public static String extrairResumo(String json) {
        try {
            JSONArray array = new JSONArray(json);
            JSONObject pedido = array.getJSONObject(0);
            JSONObject cliente = pedido.getJSONObject("dados_do_cliente");
            JSONObject entrega = pedido.getJSONArray("entregas").getJSONObject(0);

            StringBuilder sb = new StringBuilder();
            sb.append("Pedido: ").append(pedido.getString("codigo")).append("\n");
            sb.append("Cliente: ").append(cliente.getString("nome")).append("\n");
            sb.append("CPF/CNPJ: ").append(cliente.getString("cpf_cnpj")).append("\n");
            sb.append("Total: R$ ").append(pedido.getString("total_pedido")).append("\n");
            sb.append("Canal: ").append(pedido.getString("canal")).append("\n");
            sb.append("Etiqueta: ").append(entrega.getString("etiqueta")).append("\n");

            return sb.toString();
        } catch (Exception e) {
            return "Erro ao processar JSON: " + e.getMessage();
        }
    }
}
