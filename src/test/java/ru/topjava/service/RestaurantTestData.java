package ru.topjava.service;

import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.topjava.entity.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final int RESTAURANT_ID = START_SEQ + 2;

    public static final Restaurant restaurantWithTodayMenu = new Restaurant(100002, "first restaurant", true, new Dish("first dish First restaurant", 1.01, LocalDate.now()), new Dish("second dish First restaurant", 2.1, LocalDate.now()));
    private static final Restaurant anotherRestaurantWithTodayMenu = new Restaurant(100003, "second restaurant", true, new Dish("first dish Second restaurant", 1.02), new Dish("second dish Second restaurant", 2.2));
    public static final List<Restaurant> allRestaurantWithTodayMenu = new ArrayList<>(Arrays.asList(anotherRestaurantWithTodayMenu, restaurantWithTodayMenu));

    public static List<Restaurant> getAllRestaurantWithHistoryDish() {
        Restaurant first = new Restaurant(restaurantWithTodayMenu);
        first.addDish(new Dish("Late Dish First restaurant", 3.11, LocalDate.of(2020, 10, 20)));
        Restaurant second = new Restaurant(anotherRestaurantWithTodayMenu);
        second.addDish(new Dish("Late Dish Second restaurant", 3.22, LocalDate.of(2020, 10, 20)));
        Restaurant onlyHistoryDish = new Restaurant(100004, "Latest restaurant", true, new Dish("first Dish Latest restaurant", 3.22, LocalDate.of(2020, 10, 20)), new Dish("second Dish Latest restaurant", 3.22, LocalDate.of(2020, 10, 20)), new Dish("Latest Dish Latest restaurant", 3.22, LocalDate.of(2020, 10, 15)));
        return new ArrayList<>(Arrays.asList(first, second, onlyHistoryDish));
    }

    public static Restaurant getOneWithHistoryDish(){
        Restaurant result = new Restaurant(restaurantWithTodayMenu);
        result.addDish(new Dish("Late Dish First restaurant", 3.11, LocalDate.of(2020, 10, 20)));
        return result;
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "new Restaurant", true, new Dish("new Restaurant, first dish", 11.01), new Dish("new Restaurant, second Dish", 12.01), new Dish("new Restaurant, old Dish", 13.01, LocalDate.of(2020, 11, 11)));
    }
}
