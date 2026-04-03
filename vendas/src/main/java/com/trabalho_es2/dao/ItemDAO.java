package com.trabalho_es2.dao;

import com.trabalho_es2.model.Item;

import com.trabalho_es2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ItemDAO {
    // CREATE
    public void adicionar(Item item) {
       Session session = HibernateUtil.getFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.persist(item);

        tx.commit();
        session.close(); 
    }

    // DELETE
    public void remover(Item item) {
        Session session = HibernateUtil.getFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.remove(item);

        tx.commit();
        session.close();
    }
}
