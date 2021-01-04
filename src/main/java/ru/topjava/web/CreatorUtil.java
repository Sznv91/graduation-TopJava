package ru.topjava.web;

import org.springframework.stereotype.Component;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.entity.User;

import java.util.Collections;

@Component
public class CreatorUtil {

    @org.jetbrains.annotations.NotNull
    public <T> T get(Class<T> clazz, Object fromJson) {

        switch (clazz.getCanonicalName()) {
            case "ru.topjava.entity.Restaurant":
                return (T) getRestaurant((Restaurant) fromJson);
            case "ru.topjava.entity.User":
                return (T) getUser((User) fromJson);
//            case "ru.topjava.entity.Dish" : return (T) getDish(fromJson);
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
        return updateRestaurant(fromJson);
    }

    private Restaurant updateRestaurant(Restaurant fromJson) {
        Restaurant result = new Restaurant(fromJson.getId(), fromJson.getName());
        if (fromJson.getMenu() != null && !fromJson.getMenu().isEmpty()) {
            result.setMenu(fromJson.getMenu());
        }
        if (fromJson.isEnable() != null) {
            result.setEnable(fromJson.isEnable());
        }
        return result;
    }
}
