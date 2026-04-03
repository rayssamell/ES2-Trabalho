package com.trabalho_es2.service;
import com.google.gson.Gson;
import com.trabalho_es2.dao.ProdutoDAO;
import com.trabalho_es2.model.Produto;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/listarProdutos")
public class ListarProdutosService extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ESSENCIAL: Libera o acesso para o Live Server do VS Code
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            ProdutoDAO dao = new ProdutoDAO();
            List<Produto> lista = dao.consultar(); // Certifique-se que o DAO não retorna null

            Gson gson = new Gson();
            String json = gson.toJson(lista);
            response.getWriter().write(json);
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"erro\": \"" + e.getMessage() + "\"}");
            e.printStackTrace(); // Olhe o console do Eclipse/IntelliJ para ver o erro real
        }
    }
}