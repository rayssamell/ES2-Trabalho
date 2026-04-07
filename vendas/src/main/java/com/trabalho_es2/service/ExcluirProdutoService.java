package com.trabalho_es2.service;

import com.trabalho_es2.dao.ProdutoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/excluirProduto/*")
public class ExcluirProdutoService extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            // pega o ID da URL
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"erro\":\"ID não informado\"}");
                return;
            }

            Long id = Long.parseLong(pathInfo.substring(1));

            ProdutoDAO dao = new ProdutoDAO();
            dao.excluir(id);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"mensagem\":\"Produto excluído\"}");

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "DELETE, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}