package com.trabalho_es2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trabalho_es2.dao.ItemDAO;
import com.trabalho_es2.dao.PedidoDAO;
import com.trabalho_es2.dao.ProdutoDAO;
import com.trabalho_es2.model.Item;
import com.trabalho_es2.model.Pedido;
import com.trabalho_es2.model.Produto;
import com.trabalho_es2.model.ProdutoEletronico;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.List;

import com.sun.net.httpserver.HttpServer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;


public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/listarProdutos", exchange -> {
    // 1. PERMISSÕES DE ACESSO (CORS) - ADICIONE TODAS ESTAS
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");

    // 2. TRATAR O "PEDIDO DE PERMISSÃO" DO NAVEGADOR
    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
        exchange.sendResponseHeaders(204, -1);
        return;
    }

    // 3. SEU CÓDIGO DE CONSULTA ORIGINAL
    try {
        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> lista = dao.consultar();
        String json = new Gson().toJson(lista);
        
        byte[] response = json.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
});
        server.setExecutor(null);
        System.out.println("Servidor rodando na porta 8080!");
        server.start();
    }
}