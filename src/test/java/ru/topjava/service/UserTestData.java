package ru.topjava.service;

import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.entity.User;

import java.time.LocalDate;
import java.util.*;

import static ru.topjava.entity.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    //public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "meals");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;
    public static final int RESTAURANT_ID = START_SEQ + 5;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);

    public static final Restaurant restaurantWithTodayMenu = new Restaurant(100002, "first restaurant", new Dish("first dish First restaurant", 1.01, LocalDate.now()), new Dish("second dish First restaurant", 2.1, LocalDate.now()));
    private static final Restaurant anotherRestaurantWithTodayMenu = new Restaurant(100003, "second restaurant", new Dish("first dish Second restaurant", 1.02), new Dish("second dish Second restaurant", 2.2));
    public static final List<Restaurant> allRestaurantWithTodayMenu = new ArrayList<>(Arrays.asList(anotherRestaurantWithTodayMenu, restaurantWithTodayMenu));

    public static final List<Restaurant> getAllRestaurantWithHistoryDish() {
        Restaurant first = new Restaurant(restaurantWithTodayMenu);
        first.addDish(new Dish("Late Dish First restaurant", 3.11, LocalDate.of(2020, 10, 20)));
        Restaurant second = new Restaurant(anotherRestaurantWithTodayMenu);
        second.addDish(new Dish("Late Dish Second restaurant", 3.22, LocalDate.of(2020, 10, 20)));
        Restaurant onlyHistoryDish = new Restaurant(100004, "Latest restaurant", new Dish("first Dish Latest restaurant", 3.22, LocalDate.of(2020, 10, 20)), new Dish("second Dish Latest restaurant", 3.22, LocalDate.of(2020, 10, 20)), new Dish("Latest Dish Latest restaurant", 3.22, LocalDate.of(2020, 10, 15)));
        return new ArrayList<>(Arrays.asList(second, first, onlyHistoryDish));
    }

    public static final Restaurant getOneWithHistoryDish(){
        Restaurant result = new Restaurant(restaurantWithTodayMenu);
        result.addDish(new Dish("Late Dish First restaurant", 3.11, LocalDate.of(2020, 10, 20)));
        return result;
    }

    public static Restaurant getNewRestaurant() {
        return new Restaurant(null, "new Restaurant", new Dish("new Restaurant, first dish", 11.01), new Dish("new Restaurant, second Dish", 12.01), new Dish("new Restaurant, old Dish", 13.01, LocalDate.of(2020, 11, 11)));
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

}
