package ru.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.Restaurant;
import ru.topjava.to.RestaurantTo;
import ru.topjava.to.UserTo;
import ru.topjava.utils.NotFoundException;
import ru.topjava.utils.PermissionException;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.service.RestaurantTestData.RESTAURANT_ID;
import static ru.topjava.service.RestaurantTestData.allRestaurantWithTodayMenu;
import static ru.topjava.service.RestaurantTestData.getAllRestaurantWithHistoryDish;
import static ru.topjava.service.RestaurantTestData.getNew;
import static ru.topjava.service.RestaurantTestData.getOneWithHistoryDish;
import static ru.topjava.service.RestaurantTestData.restaurantWithTodayMenu;

@SpringJUnitConfig(GraduationJpaConfig.class)
@Sql(scripts = {"classpath:initDB_H2.sql", "classpath:populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
class RestaurantServiceTest {

    @Autowired
    RestaurantService service;

    @Test
    void createByAdmin() {
        Restaurant expect = getNew();
        Restaurant actual = service.create(expect, UserTo.getUserTo(UserTestData.admin));
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void createByUser() {
        assertThrows(PermissionException.class, () -> service.create(getNew(), UserTo.getUserTo(UserTestData.user)));
    }

    @Test
    void setDisable() {
        Restaurant expect = service.getOneWithHistoryMenu(RESTAURANT_ID);
        expect.setEnable(false);
        Restaurant actual = service.update(expect, UserTo.getUserTo(UserTestData.admin));
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(actual));

    }

    @Test
    void getOneWithTodayMenu() {
        Restaurant actual = service.getOneWithTodayMenu(RESTAURANT_ID);
        assertEquals(RestaurantTo.getRestaurantTo(restaurantWithTodayMenu), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void getOneWithHistory() {
        Restaurant expect = getOneWithHistoryDish();
        Restaurant actual = service.getOneWithHistoryMenu(100002);
        assertEquals(RestaurantTo.getRestaurantTo(expect), RestaurantTo.getRestaurantTo(actual));
    }

    @Test
    void getNotExist() {
        assertThrows(NotFoundException.class, () -> service.getOneWithTodayMenu(UserTestData.NOT_FOUND));
    }

    @Test
    void getAllWithTodayMenu() {
        List<Restaurant> actual = service.getAllWithTodayMenu();

        List<RestaurantTo> expectTo = allRestaurantWithTodayMenu.stream().map(RestaurantTo::getRestaurantTo).collect(Collectors.toList());
        List<RestaurantTo> actualTo = actual.stream().map(RestaurantTo::getRestaurantTo).collect(Collectors.toList());

        assertEquals(expectTo, actualTo);
    }

    @Test
    void getAllWithHistoryMenu() {
        List<Restaurant> expect = getAllRestaurantWithHistoryDish();
        List<Restaurant> actual = service.getAllWithHistoryMenu();

        List<RestaurantTo> expectTo = expect.stream().map(RestaurantTo::getRestaurantTo).collect(Collectors.toList());
        List<RestaurantTo> actualTo = actual.stream().map(RestaurantTo::getRestaurantTo).collect(Collectors.toList());

        assertEquals(expectTo, actualTo);
    }
}