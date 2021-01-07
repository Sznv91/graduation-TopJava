package ru.topjava.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.service.RestaurantTestData;
import ru.topjava.service.UserTestData;
import ru.topjava.service.VoteTestData;
import ru.topjava.to.RestaurantTo;
import ru.topjava.to.UserTo;
import ru.topjava.to.VoteTo;
import ru.topjava.utils.PermissionException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.topjava.service.RestaurantTestData.RESTAURANT_ID;
import static ru.topjava.service.RestaurantTestData.getNew;
import static ru.topjava.service.RestaurantTestData.getOneWithHistoryDish;
import static ru.topjava.service.RestaurantTestData.restaurantWithTodayMenu;

@SpringJUnitConfig(GraduationJpaConfig.class)
@Sql(scripts = {"classpath:initDB_H2.sql", "classpath:populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
class ApplicationControllerTest {

    @Autowired
    private ApplicationController controller;

    @Test
    void saveUser() {
        User expect = UserTestData.getNew();
        User actual = controller.saveUser(expect);
        expect.setId(100007);
        assertEquals(UserTo.getUserTo(expect), UserTo.getUserTo(actual));
    }

    @Test
    void getUser() {
        User expect = UserTestData.admin;
        User actual = controller.getUser(UserTestData.ADMIN_ID);
        assertEquals(UserTo.getUserTo(expect), UserTo.getUserTo(actual));
    }

    @Test
    void saveRestaurantByAdmin() {
        Restaurant expect = getNew();
        Restaurant actual = controller.saveRestaurant(expect, UserTo.getUserTo(UserTestData.admin));
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void saveRestaurantByUser() {
        Restaurant expect = getNew();
        assertThrows(PermissionException.class, () -> controller.saveRestaurant(expect, UserTo.getUserTo(UserTestData.user)));
    }

    @Test
    void getRestaurantWithTodayMenu() {
        Restaurant actual = controller.getOneRestaurantWithTodayMenu(RESTAURANT_ID);
        assertEquals(RestaurantTo.getRestaurantTo(restaurantWithTodayMenu), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void getRestaurantWithHistoryMenu() {
        Restaurant expect = getOneWithHistoryDish();
        Restaurant actual = controller.getOneRestaurantWithHistoryMenu(RESTAURANT_ID);
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void saveVote() {
        Vote expect = VoteTestData.newVote;
        Vote actual = controller.saveVote(RESTAURANT_ID, UserTestData.USER_ID);
        expect.setId(VoteTestData.newVoteId);
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));
    }

    @Test
    void votedToday() {
        assertTrue(controller.votedToday(UserTestData.ADMIN_ID));
        assertFalse(controller.votedToday(UserTestData.USER_ID));
    }

    @Test
    void oneRestaurantVoteCounterToday() {
        Restaurant restaurant = controller.getOneRestaurantWithTodayMenu(RESTAURANT_ID);
        assertEquals(1, restaurant.getVoteCount());

        controller.saveVote(RESTAURANT_ID, UserTestData.USER_ID);
        restaurant = controller.getOneRestaurantWithTodayMenu(RESTAURANT_ID);
        assertEquals(2, restaurant.getVoteCount());
    }

    @Test
    void allRestaurantVoteCounterToday() {
        List<Restaurant> actual = controller.getRestaurantsWithTodayMenu();
        assertEquals(1, actual.get(0).getVoteCount());
        controller.saveVote(actual.get(0).getId(), UserTestData.USER_ID);
        actual = controller.getRestaurantsWithTodayMenu();
        assertEquals(2, actual.get(0).getVoteCount());
        assertEquals(0, actual.get(1).getVoteCount());
        User newUser = new User(null, "new user", "new email", "123321", Role.USER);
        newUser = controller.saveUser(newUser);
        controller.saveVote(actual.get(1).getId(), newUser.getId());
        actual = controller.getRestaurantsWithTodayMenu();
        assertEquals(2, actual.get(0).getVoteCount());
        assertEquals(1, actual.get(1).getVoteCount());

    }

    @Test
    void allRestaurantVoteCounterHistory() {
        List<Restaurant> actual = controller.getRestaurantsWithHistory();
        System.out.println(actual.get(0).getId() + " Restaurant ID");
        assertEquals(2, actual.get(0).getVoteCount());
    }

    @Test
    public void addDishes() {
        List<Dish> dishList = new ArrayList<Dish>();
        dishList.add(new Dish("added Dish", 15.14));
        dishList.add(new Dish("qwe", 123));
        controller.addDishes(RESTAURANT_ID, dishList, UserTo.getUserTo(UserTestData.admin));
        Restaurant expect = restaurantWithTodayMenu;
        expect.addDish(dishList);
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(controller.getOneRestaurantWithTodayMenu(RESTAURANT_ID)));
    }
}