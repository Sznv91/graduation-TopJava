package ru.topjava.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.controller.ApplicationController;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.service.VoteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestWebController extends ApplicationController {

    private final CreatorUtil creatorUtil;

    public RestWebController(UserService userService, RestaurantService restaurantService, VoteService voteService, CreatorUtil creatorUtil) {
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

    @RequestMapping("/restaurants/{restaurantId}/update")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> updateRestaurantWithLocation(@PathVariable int restaurantId, @RequestBody Restaurant restaurant) {
        Restaurant created;
        if (restaurant.getId() != null && restaurant.getId().equals(restaurantId)) {
            created = super.saveRestaurant(creatorUtil.update(Restaurant.class, restaurant), SecurityUtil.authUserId());
        } else {
            throw new UnsupportedOperationException("JSON body not correct");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
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

    //Todo make create dish list and add to Restaurant.

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
