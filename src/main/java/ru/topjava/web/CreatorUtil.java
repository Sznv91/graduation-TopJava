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
        fromJson.setId(null);
        if (checkNullDishList(fromJson)) {
            fromJson.setMenu(getDishList(fromJson.getMenu()));
        }
        return updateRestaurant(fromJson);
    }

    private Restaurant updateRestaurant(Restaurant fromJson) {
        Restaurant result = new Restaurant(fromJson.getId(), fromJson.getName());
        if (checkNullDishList(fromJson)) {
            result.setMenu(fromJson.getMenu());
        }
        if (fromJson.isEnable() != null) {
            result.setEnable(fromJson.isEnable());
        }
        return result;
    }

    public List<Dish> getDishList(List<Dish> fromJson) {
        return fromJson.parallelStream().map(dish -> new Dish(dish.getName(), dish.getCost())).collect(Collectors.toList());
    }

    private boolean checkNullDishList(Restaurant fromJson) {
        return fromJson.getMenu() != null && !fromJson.getMenu().isEmpty();
    }
}
