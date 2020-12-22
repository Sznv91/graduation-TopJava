package ru.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.to.VoteTo;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(GraduationJpaConfig.class)
@Sql(scripts = "classpath:populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class VoteServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void save() {
        Restaurant restaurant = RestaurantTestData.restaurantWithTodayMenu;
        User user = UserTestData.user;
        Vote expect = new Vote(100005, restaurant, user);
        Vote actual = service.save(expect);
        //Vote actual = service.get(expect.getId());
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));
    }

    @Test
    void update() {
        Vote expect = new Vote(100005, RestaurantTestData.anotherRestaurantWithTodayMenu, UserTestData.user);
        LocalDateTime limiter = LocalDateTime.now().plusHours(1);
        Vote actual = service.saveWithCustomDateLimiter(expect, limiter);
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));

    }

    @Test
    void updateWithoutVoteId() {
        Vote expect = new Vote(RestaurantTestData.anotherRestaurantWithTodayMenu, UserTestData.user);
        LocalDateTime limiter = LocalDateTime.now().plusHours(1);
        Vote actual = service.saveWithCustomDateLimiter(expect,limiter);
        expect.setId(100005);
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));
    }

}