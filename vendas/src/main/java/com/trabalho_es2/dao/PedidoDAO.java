package com.trabalho_es2.dao;

import com.trabalho_es2.model.Pedido;
import com.trabalho_es2.util.HibernateUtil;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class PedidoDAO {
    // CREATE
    public void cadastrar(Pedido pedido) {
       Session session = HibernateUtil.getFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.persist(pedido);

        tx.commit();
        session.close(); 
    }

    // READ
    public List<Pedido> consultar() {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            return session.createQuery(
                "select distinct p from Pedido p left join fetch p.itens i left join fetch i.produto",
                Pedido.class
            ).list();
        }
    }
}
