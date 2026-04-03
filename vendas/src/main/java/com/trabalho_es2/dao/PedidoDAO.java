package com.trabalho_es2.dao;

import com.trabalho_es2.model.Pedido;
import com.trabalho_es2.util.HibernateUtil;
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
    public Pedido consultar(Long id) {
        Session session = HibernateUtil.getFactory().openSession();
        Pedido p = session.get(Pedido.class, id);
        session.close();
        return p;
    }
}
