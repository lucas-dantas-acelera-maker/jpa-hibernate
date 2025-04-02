package br.com.aceleramaker.application;

import br.com.aceleramaker.model.User;
import br.com.aceleramaker.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();

        User user = new User("Wrong User", "wrong@user.com");

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
        *
        * String jpql = "SELECT u from User u";
        * TypedQuery<User> query = em.createQuery(jpql, User.class);
        * query.setMaxResults(5);
        * List<User> users = query.getResultList();
        * users.forEach(u -> System.out.printf("%d - %s - %s%n", u.getId(), u.getName(), u.getEmail()));
        * */

        // Update User
        /* Update operations require a Transaction
        *  After setting the new values, the merge(objToBeUpdated) method is called to update the User info
        *
        *  em.getTransaction().begin();
        *  User userToUpdate = em.find(User.class, 7L);
        *  userToUpdate.setName("Leonardo Leitão");
        *  userToUpdate.setEmail("leo.leitao@lanche.com");
        *  em.merge(userToUpdate);
        *  em.getTransaction().commit();
        * */

        /*
        *  JPA manages the relation between Object and Entity.
        *  When the Object is retrieved from the database, JPA will sync the Object and the Entity.
        *  If the Object is updated, even if the merge() is not called, the Entity will be synchronized.
        *  We can disable this management, by calling the EntityManager.detach(Object).
        *  The merge() method must be called to persist a detached Object.
        * */

        // Delete User


        User foundUser = em.find(User.class, 8L);
        System.out.println("Found user: ");
        System.out.println(foundUser.getId() + " - " + foundUser.getName() + " - " + foundUser.getEmail());

        if (foundUser != null) {
            em.getTransaction().begin();
            em.remove(foundUser);
            em.getTransaction().commit();
            System.out.println("User deleted.");
        }

        em.close();
    }
}