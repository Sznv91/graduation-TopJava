package ru.topjava.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.AuthorizedUser;
import ru.topjava.entity.User;
import ru.topjava.repository.UserRepository;
import ru.topjava.utils.NotFoundException;


@Service("userService")
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

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

    public User getByEmail (String email){
        User u = repository.getByEmail(email);
        if (u != null){
            return u;
        } else {
            throw new NotFoundException("User " + email + " is not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email);
        if(user != null){
            return new AuthorizedUser(user);
        }
        throw new UsernameNotFoundException("User " + email + " is not found");
    }
}
