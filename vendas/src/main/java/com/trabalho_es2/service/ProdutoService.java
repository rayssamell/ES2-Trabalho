package com.trabalho_es2.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.trabalho_es2.dao.ProdutoDAO;
import com.trabalho_es2.model.ProdutoEletronico;
import com.trabalho_es2.model.ProdutoPerecivel;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/cadastrarProduto")
public class ProdutoService extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");


        StringBuilder jb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        }

        String jsonString = jb.toString();
        Gson gson = new Gson();
        ProdutoDAO dao = new ProdutoDAO();
        
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        String tipo = jsonObject.get("tipo").getAsString();

        try {
            if ("ELETRONICO".equals(tipo)) {
                ProdutoEletronico eletronico = gson.fromJson(jsonString, ProdutoEletronico.class);
                dao.cadastrar(eletronico);
            } else if ("PERECIVEL".equals(tipo)) {
                ProdutoPerecivel perecivel = gson.fromJson(jsonString, ProdutoPerecivel.class);
                dao.cadastrar(perecivel);
            }

            response.setStatus(200);
            response.getWriter().write("{\"mensagem\": \"Produto salvo no MySQL via Hibernate!\"}");
            
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}

