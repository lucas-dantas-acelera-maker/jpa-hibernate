package br.com.aceleramaker.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class DAO<E> {
    private static EntityManagerFactory emf;
    private final EntityManager em;
    private final Class<E> objClass;

    static {
        try {
            loadProperties();
            emf = Persistence.createEntityManagerFactory("jpa-hibernate");
        } catch (IOException e) {
            System.out.println("Error initializing EntityManager: " + e.getMessage());
        }
    }

    public DAO(Class<E> objClass) {
        em = emf.createEntityManager();
        this.objClass = objClass;
    }

    public DAO() {
        this(null);
    }

    private static void loadProperties() throws IOException {
        Properties props = new Properties();
        InputStream is = DAO.class.getClassLoader().getResourceAsStream("config.properties");

        if (is != null) {
            props.load(is);
            is.close();

            System.setProperty("db.url", props.getProperty("db.url"));
            System.setProperty("db.user", props.getProperty("db.user"));
            System.setProperty("db.password", props.getProperty("db.password"));
        } else {
            System.out.println("config.properties file not found");
        }
    }

    public DAO<E> openTransaction() {
        em.getTransaction().begin();
        return this;
    }

    public DAO<E> persistEntity(E entity) {
        em.persist(entity);
        return this;
    }

    public DAO<E> closeTransaction() {
        em.getTransaction().commit();
        return this;
    }

    public DAO<E> addEntity(E entity) {
        return this.openTransaction().persistEntity(entity).closeTransaction();
    }

    public List<E> getEntities() {
        return this.getEntities(10, 0);
    }

    public List<E> getEntities(int limit, int offset) {
        if (objClass == null) {
            throw new UnsupportedOperationException("Object class is null. Please provide an Object Class.");
        }

        String jpql = "SELECT e FROM " + objClass.getName() + " e";
        TypedQuery<E> query = em.createQuery(jpql, objClass);
        query.setMaxResults(limit);
        query.setFirstResult(offset);

        return query.getResultList();
    }

    public void close() {
        em.close();
    }
}
