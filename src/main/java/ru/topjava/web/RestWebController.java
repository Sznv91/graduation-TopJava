package ru.topjava.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.controller.ApplicationController;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.Role;
import ru.topjava.entity.User;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.service.VoteService;
import ru.topjava.to.UserTo;
import ru.topjava.utils.PermissionException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
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

    @RequestMapping("/restaurants/{restaurantId}")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> updateRestaurantWithLocation(@PathVariable int restaurantId, @RequestBody Restaurant restaurant) {
        Restaurant created;
        if (restaurant.getId() != null && restaurant.getId().equals(restaurantId)) {
            created = super.updateRestaurant(creatorUtil.update(Restaurant.class, restaurant), SecurityUtil.get().getUserTo());
        } else {
            throw new UnsupportedOperationException("JSON body not correct");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @RequestMapping("/restaurants/{restaurantId}/add_dishes")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> addDishes(@PathVariable int restaurantId, @RequestBody List<Dish> dishes) {
        Restaurant created = super.addDishes(restaurantId, creatorUtil.getDishList(dishes), SecurityUtil.get().getUserTo());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping("/restaurants/{restaurantId}/vote")
    public Restaurant vote(@PathVariable int restaurantId, HttpServletResponse response) throws IOException {
        super.saveVote(restaurantId, SecurityUtil.authUserId());
        return super.getOneRestaurantWithTodayMenu(restaurantId);
    }

    @PutMapping("/restaurants/{restaurantId}/vote")
    public Restaurant reVote(@PathVariable int restaurantId, HttpServletResponse response) throws IOException {
        super.reVote(restaurantId, SecurityUtil.authUserId());
        return super.getOneRestaurantWithTodayMenu(restaurantId);
    }

    @GetMapping("/restaurants/history")
    public List<Restaurant> getAllRestaurantsWithHistoryDish(@RequestParam("from") String from, @RequestParam("to") String to) {
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);
        return super.getRestaurantsWithHistory(start, end);
    }

    @GetMapping("/restaurants/{restaurantId}/history")
    public Restaurant getOneRestaurantsWithHistoryDish(@PathVariable int restaurantId) {
        return super.getOneRestaurantWithHistoryMenu(restaurantId);
    }

    @RequestMapping("/restaurants")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createRestaurantWithLocation(@RequestBody Restaurant restaurant) {
        Restaurant created =
                super.saveRestaurant(creatorUtil.get(Restaurant.class, restaurant), SecurityUtil.get().getUserTo());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/user")
    public UserTo getAuthUser() {
        return SecurityUtil.get().getUserTo();
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

    @RequestMapping("/user/create/admin")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createAdminWithLocation(@RequestBody User admin) {
        User toCreated = creatorUtil.get(User.class, admin);
        toCreated.setRoles(List.of(Role.ADMIN, Role.USER));
        if (super.getUser(SecurityUtil.authUserId()).getRoles().contains(Role.ADMIN)) {
            User created = super.saveUser(toCreated);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/user/{id}")
                    .buildAndExpand(created.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(created);
        } else {
            throw new PermissionException("Haven't role Admin to create new user with role Admin");
        }

    }

}
