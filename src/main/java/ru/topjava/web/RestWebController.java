package ru.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.controller.ApplicationController;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.service.VoteService;

import java.net.URI;
import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestWebController extends ApplicationController {

    private final RestaurantCreatorUtil creatorUtil;

    public RestWebController(UserService userService, RestaurantService restaurantService, VoteService voteService, RestaurantCreatorUtil creatorUtil) {
        super(userService, restaurantService, voteService);
        this.creatorUtil = creatorUtil;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurantsWithTodayMenu() {
        return super.getRestaurantsWithTodayMenu();
    }

    @GetMapping("/restaurants/{restaurantId}")
    public Restaurant getOneRestaurant(@PathVariable int restaurantId) {
        return super.getOneRestaurantWithTodayMenu(restaurantId);
    }

    @GetMapping("/restaurants/history")
    public List<Restaurant> getAllRestaurantsWithHistoryDish() {
        return super.getRestaurantsWithHistory();
    }

    @GetMapping("/restaurants/history/{restaurantId}")
    public Restaurant getOneRestaurantsWithHistoryDish(@PathVariable int restaurantId) {
        return super.getOneRestaurantWithHistoryMenu(restaurantId);
    }

    @RequestMapping("/restaurants/create")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createRestaurantWithLocation(@RequestBody Restaurant restaurant) {
        Restaurant created =
                super.saveRestaurant(creatorUtil.get(Restaurant.class, restaurant), SecurityUtil.authUserId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/user")
    public User getAuthUser() {
        return super.getUser(SecurityUtil.authUserId());
    }

    @GetMapping("/user/test_change_user/{userId}")
    public void changeUser(@PathVariable int userId) {
        SecurityUtil.setAuthUserId(userId);
    }

    @RequestMapping("/user/create")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUserWithLocation(@RequestBody User user) {
        User created = super.saveUser(creatorUtil.get(User.class, user));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

}
