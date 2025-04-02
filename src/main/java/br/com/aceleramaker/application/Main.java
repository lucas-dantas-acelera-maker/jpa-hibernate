package br.com.aceleramaker.application;

import br.com.aceleramaker.model.User;
import br.com.aceleramaker.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

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
        /*
        * em.find(Class to be found, Identifier) -> returns an Object (User, in this case)
        * User foundUser = em.find(User.class, 7L);
        * System.out.println(foundUser.getId() + " - " + foundUser.getName() + " - " + foundUser.getEmail());
        * */

        // Get All Users
        /*
        * TypedQuery<ReturnedClass> query = em.createQuery(query, returned class)
        * */
        String jpql = "SELECT u from User u";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setMaxResults(5);
        List<User> users = query.getResultList();

        users.forEach(u -> System.out.printf("%d - %s - %s%n", u.getId(), u.getName(), u.getEmail()));

        em.close();
    }
}