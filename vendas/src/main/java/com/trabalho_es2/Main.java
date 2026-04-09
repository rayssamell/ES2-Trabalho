package com.trabalho_es2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonArray; 
import com.trabalho_es2.dao.PedidoDAO;
import com.trabalho_es2.dao.ProdutoDAO;
import com.trabalho_es2.model.Item;
import com.trabalho_es2.model.Pedido;
import com.trabalho_es2.model.Produto;
import com.trabalho_es2.model.ProdutoEletronico;
import com.trabalho_es2.model.ProdutoPerecivel;
import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // =========================
        // LISTAR PRODUTOS
        // =========================
        server.createContext("/listarProdutos", exchange -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            try {
                ProdutoDAO dao = new ProdutoDAO();
                List<Produto> lista = dao.consultar();

                Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.time.LocalDate.class, 
                    (com.google.gson.JsonSerializer<java.time.LocalDate>) 
                    (src, typeOfSrc, context) -> 
                        new com.google.gson.JsonPrimitive(src.toString()))
                .create();

                String json = gson.toJson(lista);
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

        // =========================
        // EXCLUIR PRODUTO
        // =========================
        server.createContext("/excluirProduto", exchange -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {

                try {
                    String path = exchange.getRequestURI().getPath();

                    String[] partes = path.split("/");

                    if (partes.length < 3) {
                        String erro = "{\"erro\":\"ID não informado\"}";
                        exchange.sendResponseHeaders(400, erro.length());
                        exchange.getResponseBody().write(erro.getBytes());
                        exchange.close();
                        return;
                    }

                    Long id = Long.parseLong(partes[2]);

                    ProdutoDAO dao = new ProdutoDAO();
                    dao.excluir(id);

                    String response = "{\"mensagem\":\"Produto excluído\"}";
                    byte[] bytes = response.getBytes("UTF-8");

                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.close();

                } catch (Exception e) {
                    e.printStackTrace();

                    String erro = "{\"erro\":\"" + e.getMessage() + "\"}";
                    byte[] bytes = erro.getBytes("UTF-8");

                    exchange.sendResponseHeaders(500, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.close();
                }
            }
        });

        // Cadastrar Produto
        server.createContext("/cadastrarProduto", exchange -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                try {
                    // 📥 LER JSON
                    String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");

                    JsonObject json = new Gson().fromJson(body, JsonObject.class);

                    String tipo = json.get("tipo").getAsString();

                    Produto produto;

                    if ("ELETRONICO".equalsIgnoreCase(tipo)) {

                        produto = new Gson().fromJson(body, ProdutoEletronico.class);

                    } else {

                        ProdutoPerecivel p = new ProdutoPerecivel();

                        p.setNome(json.get("nome").getAsString());
                        p.setPreco(json.get("preco").getAsDouble());
                        p.setEstoque(json.get("estoque").getAsInt());

                        if (json.has("dataValidade")) {
                            String data = json.get("dataValidade").getAsString();

                            if (data != null && !data.isEmpty()) {
                                p.setDataValidade(java.time.LocalDate.parse(data));
                            }
                        }

                        if (json.has("dataValidade")) {

                            String data = json.get("dataValidade").getAsString();

                            if (data != null && !data.isEmpty()) {
                                p.setDataValidade(java.time.LocalDate.parse(data));
                            }
                        }
                        produto = p;
                    }

                    ProdutoDAO dao = new ProdutoDAO();
                    dao.cadastrar(produto);

                    String response = "{\"mensagem\":\"Produto cadastrado com sucesso\"}";
                    byte[] bytes = response.getBytes("UTF-8");

                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.close();

                } catch (Exception e) {
                    e.printStackTrace();

                    String erro = "{\"erro\":\"" + e.getMessage() + "\"}";
                    byte[] bytes = erro.getBytes("UTF-8");

                    exchange.sendResponseHeaders(500, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.close();
                }
            }
        });

        // Cadastrar Pedido
        server.createContext("/cadastrarPedido", exchange -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                try {
                    String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");

                    Gson gson = new Gson();
                    JsonObject json = gson.fromJson(body, JsonObject.class);

                    Pedido pedido = new Pedido();
                    pedido.setData(java.time.LocalDate.parse(json.get("data").getAsString()));
                    pedido.setValorTotal(json.get("valorTotal").getAsDouble());

                    List<Item> itens = new java.util.ArrayList<>();

                    JsonArray jsonItens = json.getAsJsonArray("itens");

                    for (JsonElement elem : jsonItens) {
                        JsonObject obj = elem.getAsJsonObject();

                        Item item = new Item();

                        item.setQtde(obj.get("quantidade").getAsInt());
                        item.setValorItem(obj.get("valorItem").getAsDouble());

                        Long prodId = obj.getAsJsonObject("produto").get("id").getAsLong();

                        Produto produto = new ProdutoDAO().buscarPorID(prodId);
                        if (produto == null) {
                            throw new RuntimeException("Produto não encontrado ID: " + prodId);
                        }

                        item.setProduto(produto);
                        item.setPedido(pedido);

                        itens.add(item);
                    }
                    System.out.println("Itens recebidos: " + json.getAsJsonArray("itens"));
                    pedido.setItens(itens);

                    // 💾 salva tudo
                    new PedidoDAO().cadastrar(pedido);

                    String resp = "{\"mensagem\":\"Pedido salvo\"}";
                    byte[] bytes = resp.getBytes("UTF-8");

                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.close();

                } catch (Exception e) {
                    e.printStackTrace();

                    String erro = "{\"erro\":\"" + e.getMessage() + "\"}";
                    byte[] bytes = erro.getBytes("UTF-8");

                    exchange.sendResponseHeaders(500, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.close();
                }
            }
        });

        server.createContext("/listarPedidos", exchange -> {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                try {
                    List<Pedido> lista = new PedidoDAO().consultar();

                    Gson gson = new GsonBuilder()
                    .registerTypeAdapter(java.time.LocalDate.class,
                        (JsonSerializer<java.time.LocalDate>) 
                        (src, t, c) -> new JsonPrimitive(src.toString()))

                    .setExclusionStrategies(new com.google.gson.ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(com.google.gson.FieldAttributes f) {
            
                            return f.getDeclaringClass() == Item.class && f.getName().equals("pedido");
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create();

                    String json = gson.toJson(lista);
                    byte[] resp = json.getBytes("UTF-8");

                    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    exchange.sendResponseHeaders(200, resp.length);
                    
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(resp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String erro = "{\"erro\":\"" + e.getMessage() + "\"}";
                    byte[] bytes = erro.getBytes("UTF-8");
                    exchange.sendResponseHeaders(500, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.close();
                }
            }
        });

        server.setExecutor(null);
        server.start();

        System.out.println("Servidor rodando em http://localhost:8080");
    }
}