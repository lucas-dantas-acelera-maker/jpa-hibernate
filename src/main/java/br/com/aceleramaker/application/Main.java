package br.com.aceleramaker.application;

import br.com.aceleramaker.model.User;
import br.com.aceleramaker.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();

        User user = new User("Lucas Dantas", "lucas@email.com");

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        em.close();
    }
}