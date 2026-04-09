package com.trabalho_es2.dao;

import com.trabalho_es2.model.Produto;
import com.trabalho_es2.util.HibernateUtil;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProdutoDAO {
    // CREATE
    public void cadastrar(Produto produto) {
        Session session = HibernateUtil.getFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        session.persist(produto);
        tx.commit();
        
        session.close();
    }

    // READ
    public List<Produto> consultar() {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            return session.createQuery("from Produto", Produto.class).list();
        }
    }

    // UPDATE
    public void alterar(Produto produto) {
        Session session = HibernateUtil.getFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.merge(produto);

        tx.commit();
        session.close();
    }

    // DELETE
    public void excluir(Long produtoId) {

        Session session = HibernateUtil.getFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            session.createMutationQuery("delete from Item where produto.id = :id")
                .setParameter("id", produtoId)
                .executeUpdate();

            Produto produto = session.get(Produto.class, produtoId);

            if (produto != null) {
                session.remove(produto);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    //Para Visualização
    public Produto buscarPorID(Long id) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            return session.get(Produto.class, id);
        }
    }
}
