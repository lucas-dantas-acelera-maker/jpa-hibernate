# Notes on JPA Operations

## 1. Configuring the EntityManager
The `EntityManager` is obtained through the `JPAUtil` class:

```java
EntityManager em = JPAUtil.getEntityManager();
```

### JPAUtil Class
The `JPAUtil` class manages the `EntityManagerFactory` and ensures that properties are loaded from `config.properties`.

```java
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
```

## 2. Creating (Persisting) a User
To save a new user in the database, a transaction must be started, and the `persist()` method should be used:

```java
em.getTransaction().begin();
em.persist(user);
em.getTransaction().commit();
System.out.println("Generated ID: " + user.getId());
```

## 3. Retrieving a User by Identifier
The `find(Class, Identifier)` method returns an instance of the specified class:

```java
User foundUser = em.find(User.class, 7L);
System.out.println(foundUser.getId() + " - " + foundUser.getName() + " - " + foundUser.getEmail());
```

## 4. Retrieving All Users
Fetching multiple records is done using `TypedQuery` and JPQL:

```java
String jpql = "SELECT u from User u";
TypedQuery<User> query = em.createQuery(jpql, User.class);
query.setMaxResults(5);
List<User> users = query.getResultList();
users.forEach(u -> System.out.printf("%d - %s - %s%n", u.getId(), u.getName(), u.getEmail()));
```

## 5. Updating a User
To update an entity, a transaction must be started, and the `merge()` method should be used:

```java
em.getTransaction().begin();
User userToUpdate = em.find(User.class, 7L);
userToUpdate.setName("Leonardo Leitão");
userToUpdate.setEmail("leo.leitao@lanche.com");
em.merge(userToUpdate);
em.getTransaction().commit();
```

### JPA Management Note
- JPA manages the relationship between Objects and Entities.
- If an object is retrieved from the database, any changes made will be synchronized without calling `merge()`.
- To disable this automatic synchronization, use `em.detach(Object)`.
- Detached objects must be persisted using `merge()`.

## 6. Deleting a User
To remove an entity, it must first be retrieved from the database and then removed within a transaction:

```java
User foundUser = em.find(User.class, 8L);
System.out.println("Found user: ");
System.out.println(foundUser.getId() + " - " + foundUser.getName() + " - " + foundUser.getEmail());

if (foundUser != null) {
    em.getTransaction().begin();
    em.remove(foundUser);
    em.getTransaction().commit();
    System.out.println("User deleted.");
}
```

## 7. Closing the EntityManager
Always close the `EntityManager` after operations:

```java
em.close();
```

