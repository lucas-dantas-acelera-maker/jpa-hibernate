# Data Access Object

Data Access Object (DAO) is a pattern of development that abstracts the connection to a Database.
DAOs work directly in the persistence layer, handling Database connection/operations.

---

## DAO Implementation
We can implement a generic type of DAO, accepting a type E to represent an Entity.

### Attributes
```java
    private static EntityManagerFactory emf;
    private final EntityManager em;
    private final Class<E> objClass;
```
These are the attributes of our generic DAO class.
- `EntityManagerFactory emf`: Class used to manage `EntityManager` in the application
- `EntityManager em`: Responsible for the Database Interaction
- `Class<E> objClass`: Object representing the Entity Class handled by the DAO

### Initialization
```java
    static {
        try {
            loadProperties();
            emf = Persistence.createEntityManagerFactory("jpa-hibernate");
        } catch (IOException e) {
            System.out.println("Error initializing EntityManager: " + e.getMessage());
        }
    }
```
We use a static block to initialize the `EntityManageFactory`only once, when the class is loaded, avoiding multiple instances.
The `config.properties` file content is also load to System Variables.

---

## Class Methods
### 1. openTransaction()
```java
    public DAO<E> openTransaction() {
        em.getTransaction().begin();
        return this;
    }
```
- Starts a Transaction in the Database
- Returns the DAO instance itself to allow method chaining

### 2. persistEntity(E entity)
```java
    public DAO<E> persistEntity(E entity) {
        em.persist(entity);
        return this;
    }
```
- Persists the Entity in the Database
- Returns the DAO instance itself to allow method chaining

### 3. closeTransaction()
```java
    public DAO<E> closeTransaction() {
        em.getTransaction().commit();
        return this;
    }
```
- Commits the Transaction, saving changes to the Database
- Returns the DAO instance itself to allow method chaining

### 4. addEntity(E entity)
```java
    public DAO<E> addEntity(E entity) {
        return this.openTransaction().persistEntity(entity).closeTransaction();
    }
```
- Allows persisting an entity in a singled chained call
- Opens a transaction, add an Entity and commits the transaction

### 5. getEntities()
```java
    public List<E> getEntities() {
        return this.getEntities(10, 0);
    }
```
- Retrieves entities from the Database, with a default limit of 10 records

### 6. getEntities(int limit, int offset)
```java
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
```
Retrieves entities from Database with pagination.
- Uses JPQL to fetch entities
- Implements pagination with `limit` and `offset`
- Throws an exception if `objClass` is null

### 7. close()
```java
    public void close() {
        em.close();
    }
```
- Closes the `EntityManager` to free up resources

---

## Conclusion
The `DAO<E>`class simplifies interaction with the database, providing methods to perform **CRUD operations and transaction management**.\
Its generic structure allows reuse for different entities, reducing code duplication and improving organization for the persistence layer.


