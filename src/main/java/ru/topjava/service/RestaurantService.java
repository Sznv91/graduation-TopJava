package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.entity.User;
import ru.topjava.repository.RestaurantRepository;
import ru.topjava.utils.PermissionException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository repository;
    private final UserService userService;


    public RestaurantService(RestaurantRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional
    public Restaurant create(Restaurant restaurant, User user) {
        //User user = userService.getUser(userId);
        if (user.getRoles().contains(Role.ADMIN)) {
            return repository.save(restaurant);
        } else {
            throw new PermissionException("User id: " + user.getId() + "haven't role ADMIN");
        }
    }

    public Restaurant getOneWithTodayMenu(int id){
        return repository.getOneWithCurrentDate(id);
    }

    public Restaurant getOneWithHistoryMenu (int id){
        return repository.getOneWithHistoryDish(id);
    }

    public List<Restaurant> getAllWithTodayMenu(){
        return repository.getTodayList();
    }

    public List<Restaurant> getAllWithHistoryMenu(){
        return repository.getAllHistoryWithDish();
    }

    public Restaurant getReference (int id){
        return repository.getReference(id);
    }



}
