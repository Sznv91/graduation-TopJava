package ru.topjava.web;

import org.springframework.stereotype.Component;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreatorUtil {

    public <T> T get(Class<T> clazz, Object fromJson) {

        switch (clazz.getCanonicalName()) {
            case "ru.topjava.entity.Restaurant":
                return (T) getRestaurant((Restaurant) fromJson);
            case "ru.topjava.entity.User":
                return (T) getUser((User) fromJson);
//            case "ru.topjava.entity.Dish" : return (T) getDishList((List<Dish>) fromJson);
        }
        return null;
    }

    public <T> T update(Class<T> clazz, Object fromJson) {
        switch (clazz.getCanonicalName()) {
            case "ru.topjava.entity.Restaurant":
                return (T) updateRestaurant((Restaurant) fromJson);
//            case "ru.topjava.entity.User" : return (T) getUser((User) fromJson);
//            case "ru.topjava.entity.Dish" : return (T) getDish(fromJson);
        }
        return null;
    }

    private User getUser(User fromJson) {
        User result = new User();
        result.setName(fromJson.getName());
        result.setRoles(Collections.singleton(Role.USER));
        result.setEmail(fromJson.getEmail());
        result.setPassword(fromJson.getPassword());
        return result;
    }

    private Restaurant getRestaurant(Restaurant fromJson) {
        return restaurantBuilder(fromJson, new Restaurant(fromJson.getName()));
    }

    private Restaurant updateRestaurant(Restaurant fromJson) {
        return restaurantBuilder(fromJson, new Restaurant(fromJson.getId(), fromJson.getName()));
    }

    private Restaurant restaurantBuilder(Restaurant fromJson, Restaurant createdRestaurant) {
        if (fromJson.getMenu() != null && !fromJson.getMenu().isEmpty()) {
            createdRestaurant.setMenu(getDishList(fromJson.getMenu()));
        }
        if (fromJson.isEnable() != null) {
            createdRestaurant.setEnable(fromJson.isEnable());
        }
        return createdRestaurant;
    }

    public List<Dish> getDishList(List<Dish> dishes) {
        return dishes
                .parallelStream()
                .map(dish -> new Dish(dish.getName(), dish.getCost()))
                .collect(Collectors.toList());
    }
}
