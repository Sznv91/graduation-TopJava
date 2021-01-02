package ru.topjava.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.topjava.controller.ApplicationController;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.service.VoteService;

import java.util.List;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestWebController extends ApplicationController {

    public RestWebController(UserService userService, RestaurantService restaurantService, VoteService voteService) {
        super(userService, restaurantService, voteService);
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurantsWithTodayMenu() {
        return super.getRestaurantsWithTodayMenu();
    }

    @GetMapping("/restaurants/{restaurantId}")
    public Restaurant getOneRestaurant(@PathVariable int restaurantId) {
        return super.getOneRestaurantWithTodayMenu(restaurantId);
    }

    @GetMapping("/user")
    public User getAuthUser() {
        return super.getUser(SecurityUtil.authUserId());
    }

}
