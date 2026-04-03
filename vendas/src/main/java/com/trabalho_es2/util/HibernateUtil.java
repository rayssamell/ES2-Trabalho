package com.trabalho_es2.util;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory factory;

    static {
        try {
            factory = new Configuration()
                     .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getFactory() {
        return factory;
    }

    public static SessionFactory getSessionFactory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSessionFactory'");
    }
}