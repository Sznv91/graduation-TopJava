package ru.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.Restaurant;
import ru.topjava.to.RestaurantTo;
import ru.topjava.utils.NotFoundException;
import ru.topjava.utils.PermissionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(GraduationJpaConfig.class)
@Sql(scripts = "classpath:populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class RestaurantServiceTest {

    @Autowired
    RestaurantService service;

    @Test
    void createByAdmin() {
        Restaurant expect = UserTestData.getNewRestaurant();
        Restaurant actual = service.create(expect, UserTestData.ADMIN_ID);
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void createByUser() {
        assertThrows(PermissionException.class, () -> service.create(UserTestData.getNewRestaurant(), UserTestData.USER_ID));
    }

    @Test
    void getOneWithTodayMenu() {
        Restaurant expect = UserTestData.restaurantWithTodayMenu;
        Restaurant actual = service.getOneWithTodayMenu(100002);
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void getOneWithHistory(){
        Restaurant expect = UserTestData.getOneWithHistoryDish();
        Restaurant actual = service.getOneWithHistoryMenu(100002);
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void getNotExist() {
        assertThrows(NotFoundException.class, () -> service.getOneWithTodayMenu(UserTestData.NOT_FOUND));
    }

    @Test
    void getAllWithTodayMenu() {
        List<Restaurant> expect = UserTestData.allRestaurantWithTodayMenu;
        List<Restaurant> actual = service.getAllWithTodayMenu();

        List<RestaurantTo> expectTo = expect.stream().map(RestaurantTo::getRestaurantTo).collect(Collectors.toList());
        List<RestaurantTo> actualTo = actual.stream().map(RestaurantTo::getRestaurantTo).collect(Collectors.toList());

        assertEquals(expectTo, actualTo);
    }

    @Test
    void getAllWithHistoryMenu() {
        List<Restaurant> expect = UserTestData.getAllRestaurantWithHistoryDish();
        List<Restaurant> actual = service.getAllWithHistoryMenu();

        List<RestaurantTo> expectTo = expect.stream().map(RestaurantTo::getRestaurantTo).collect(Collectors.toList());
        List<RestaurantTo> actualTo = actual.stream().map(RestaurantTo::getRestaurantTo).collect(Collectors.toList());

        assertEquals(expectTo, actualTo);
    }
}