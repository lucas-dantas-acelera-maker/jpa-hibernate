// Classe JPAUtil.java
package br.com.aceleramaker.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JPAUtil {
    private final static EntityManagerFactory emf;

    static {
        loadProperties();
        emf = Persistence.createEntityManagerFactory("jpa-hibernate");
    }

    private static void loadProperties() {
        try {
            Properties props = new Properties();
            InputStream is = JPAUtil.class.getClassLoader().getResourceAsStream("config.properties");

            if (is != null) {
                props.load(is);
                is.close();

                System.setProperty("db.url", props.getProperty("db.url"));
                System.setProperty("db.user", props.getProperty("db.user"));
                System.setProperty("db.password", props.getProperty("db.password"));
            } else {
                System.err.println("config.properties file not found.");
            }
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}