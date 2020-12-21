package ru.topjava.service;

import org.springframework.data.jpa.repository.Modifying;
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

    public User get(int id) {
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

    @Modifying
    public Boolean delete(int id){
        return repository.delete(id);
    }

    public User getReference (int id){
        return repository.getReference(id);
    }
}
