package br.com.aceleramaker.application;

import br.com.aceleramaker.model.User;
import br.com.aceleramaker.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();

        User user = new User("Leonardo", "leonardo@lanche.com");

        // Save User
        /*
        * em.getTransaction().begin();
        * em.persist(user);
        * em.getTransaction().commit();
        * System.out.println("Generated ID: " + user.getId());
        * */

        // Get User By Identifier
        // em.find(Class to be found, Identifier) -> returns an Object (User, in this case)
        User foundUser = em.find(User.class, 7L);
        System.out.println(foundUser.getId() + " - " + foundUser.getName() + " - " + foundUser.getEmail());

        em.close();
    }
}