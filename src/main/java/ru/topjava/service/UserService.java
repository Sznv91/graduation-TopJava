package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.entity.User;
import ru.topjava.repository.UserRepository;



@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUser(int id) {
        return repository.getById(id);
    }

    @Transactional
    public User save(User user) {
        return repository.create(user);
    }

    @Transactional
    public User update(User user) {
        return repository.update(user);
    }

    public Boolean delete(int id){
        return repository.delete(id);
    }

    public Restaurant addRestaurant(User user, Restaurant restaurant) {
        if (user.getRoles().equals(Role.ADMIN)) {

        } else {
            throw new RuntimeException();
        }
        return null;
    }
}
