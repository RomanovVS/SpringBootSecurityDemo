package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOImp implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
        entityManager.flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> showAllUsers() {
        return (List<User>) entityManager.createQuery("SELECT user from User user").getResultList();
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> user = entityManager.createQuery("SELECT user from User user where user.username= :username", User.class);
        user.setParameter("username", username);
        return user.getSingleResult();
    }

    @Override
    public void editUser(Long id, User user) {
        entityManager.merge(user);
        entityManager.flush();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }
}