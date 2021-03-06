package ru.topjava.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.topjava.entity.User;
import ru.topjava.utils.ExistException;
import ru.topjava.utils.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

@EnableCaching
@Repository
public class UserRepository {


    @PersistenceContext
    private EntityManager em;

    @Cacheable(value = "userCache")
    public User getByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
    }

    public User getById(int id) {
        User result = em.find(User.class, id);
        if (result != null) {
            return result;
        } else {
            throw new NotFoundException("User " + id + " not found");
        }
    }


    public User create(@NotNull User user) {
        if (user.isNew()) {
            try {
                em.createQuery("SELECT u.id FROM User u WHERE u.email=:email").setParameter("email", user.getEmail()).getSingleResult();
                throw new ExistException("User with email" + user.getEmail() + " already exist");
            } catch (NoResultException e) {
                em.persist(user);
                return em.find(User.class, user.getId());
            }
        } else {
            throw new ExistException("User " + user.getId() + " already exist");
        }
    }

    public User update(User user) {
        Assert.notNull(user, "User must not be null");
        if (user.getId() != null) {
            return em.merge(user);
        } else {
            throw new NotFoundException("User " + user.getName() + " have null Id");
        }
    }

    public Boolean delete(int id) {
        User u;
        try {
            u = getById(id);
            em.remove(u);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    public User getReference(int id) {
        return em.getReference(User.class, id);
    }

}
