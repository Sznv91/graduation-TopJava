package ru.topjava.web;

import ru.topjava.entity.Restaurant;

public class RestaurantCreatorUtil {

    public static Restaurant get(Restaurant fromJson) {
        Restaurant result = new Restaurant(fromJson.getName());
        if (fromJson.getMenu() != null && !fromJson.getMenu().isEmpty()) {
            result.setMenu(fromJson.getMenu());
        }
        if (fromJson.isEnable() != null) {
            result.setEnable(fromJson.isEnable());
        }
        return result;
    }
}
