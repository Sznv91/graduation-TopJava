package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.entity.User;
import ru.topjava.repository.RestaurantRepository;
import ru.topjava.utils.PermissionException;

import java.util.Dictionary;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository repository;


    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Restaurant create(Restaurant restaurant, User user) {
        if (user.getRoles().contains(Role.ADMIN)) {
            return repository.save(restaurant);
        } else {
            throw new PermissionException("User id: " + user.getId() + "haven't role ADMIN");
        }
    }

    public Restaurant getOneWithTodayMenu(int id) {
        return repository.getOneWithCurrentDate(id);
    }

    public Restaurant getOneWithHistoryMenu(int id) {
        return repository.getOneWithHistoryDish(id);
    }

    public List<Restaurant> getAllWithTodayMenu() {
        return repository.getTodayList();
    }

    public List<Restaurant> getAllWithHistoryMenu() {
        return repository.getAllHistoryWithDish();
    }

    public Restaurant getReference(int id) {
        return repository.getReference(id);
    }

    public Restaurant addDish(int restaurantId, List<Dish> dishes, User user){
        Restaurant restaurant = getOneWithHistoryMenu(restaurantId);
        restaurant.addDish(dishes);
        return create(restaurant, user);
    }


}
