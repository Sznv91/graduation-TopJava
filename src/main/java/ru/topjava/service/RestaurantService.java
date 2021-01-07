package ru.topjava.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.repository.RestaurantRepository;
import ru.topjava.to.UserTo;
import ru.topjava.utils.PermissionException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository repository;


    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Restaurant create(Restaurant restaurant, UserTo admin) {
        checkAdminRole(admin);
        return repository.save(restaurant);
    }

    @Transactional
    public Restaurant update(Restaurant restaurant, UserTo admin) {
        checkAdminRole(admin);
        return repository.update(restaurant);
    }

    private void checkAdminRole(UserTo admin) {
        if (!admin.getRoles().contains(Role.ADMIN)) {
            throw new PermissionException("User id: " + admin.getId() + "haven't role ADMIN");
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

    @Transactional
    public Restaurant addDish(int restaurantId, List<Dish> dishes, UserTo admin) {
        Restaurant restaurant = getOneWithHistoryMenu(restaurantId);
        restaurant.addDish(dishes);
        return update(restaurant, admin);
    }


}
