package ru.topjava.repository;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.topjava.entity.User;
import ru.topjava.utils.ExistException;
import ru.topjava.utils.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

@Repository
public class UserRepository {

    private final UserCrudRepository repository;

    @PersistenceContext
    private EntityManager em;

    public UserRepository(UserCrudRepository repository) {
        this.repository = repository;
    }

    public User getByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
    }

    public User getById(int id) {
        User result = repository.findById(id).orElse(null);//getById(id);
        if (result != null) {
            return result;
        } else {
            throw new NotFoundException("User " + id + " not found");
        }
    }


    public User create(@NotNull User user) {
        if (user.isNew()) {
            return new User(save(user));
        } else {
            throw new ExistException("User " + user.getId() + " already exist");
        }
    }

    public User update(User user) {
        Assert.notNull(user, "User must not be null");
        if (user.getId() != null) {
            return save(user);
        } else {
            throw new NotFoundException("User " + user.getName() + " have null Id");
        }
    }

    public Boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    public User getReference(int id) {
        return em.getReference(User.class, id);
    }

    private User save(User user) {
        return repository.save(user);
    }

}
