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
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.service.VoteService;
import ru.topjava.to.UserTo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestWebController {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final VoteService voteService;

    private final CreatorUtil creatorUtil;

    public RestWebController(UserService userService, RestaurantService restaurantService, VoteService voteService, CreatorUtil creatorUtil) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.voteService = voteService;
        this.creatorUtil = creatorUtil;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurantsWithTodayMenu() {
        return getRestaurants(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999),
                restaurantService.getAllWithTodayMenu());
    }

    @GetMapping("/restaurants/{restaurantId}")
    public Restaurant getOneRestaurant(@PathVariable int restaurantId) {
        return restaurantService.getOneWithTodayMenu(restaurantId);
    }

    @PutMapping(value = "/restaurants/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant updateRestaurant(@PathVariable int restaurantId, @RequestBody Restaurant restaurant) {
        Restaurant created;
        if (restaurant.getId() != null && restaurant.getId().equals(restaurantId)) {
            return restaurantService.update(creatorUtil.update(Restaurant.class, restaurant), SecurityUtil.get().getUserTo());
        } else {
            throw new UnsupportedOperationException("JSON body not correct");
        }
    }

    @PutMapping(value = "/restaurants/{restaurantId}/add_dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant addDishes(@PathVariable int restaurantId, @RequestBody List<Dish> dishes) {
        return restaurantService.addDish(restaurantId, creatorUtil.getDishList(dishes), SecurityUtil.get().getUserTo());
    }

    @PostMapping("/restaurants/{restaurantId}/vote")
    public Restaurant vote(@PathVariable int restaurantId, HttpServletResponse response) throws IOException {
        voteService.save(new Vote(restaurantService.getReference(restaurantId), userService.getReference(SecurityUtil.authUserId())));
        return restaurantService.getOneWithTodayMenu(restaurantId);
    }

    @PutMapping("/restaurants/{restaurantId}/vote")
    public Restaurant reVote(@PathVariable int restaurantId, HttpServletResponse response) throws IOException {
        voteService.update(new Vote(restaurantService.getReference(restaurantId), userService.getReference(SecurityUtil.authUserId())));
        return restaurantService.getOneWithTodayMenu(restaurantId);
    }

    @GetMapping("/restaurants/history")
    public List<Restaurant> getAllRestaurantsWithHistoryDish(@RequestParam("from") String from, @RequestParam("to") String to) {
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);
        return getRestaurants(LocalDateTime.of(start, LocalTime.of(0, 0, 0, 0))
                , LocalDateTime.of(end, LocalTime.of(23, 59, 59, 999999999))
                , restaurantService.getAllWithHistoryMenu(start, end));
    }

    @GetMapping("/restaurants/{restaurantId}/history")
    public Restaurant getOneRestaurantsWithHistoryDish(@PathVariable int restaurantId) {
        return restaurantService.getOneWithHistoryMenu(restaurantId);
    }

    @PostMapping(value = "/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createRestaurantWithLocation(@RequestBody Restaurant restaurant) {
        Restaurant created = restaurantService.create(creatorUtil.get(Restaurant.class, restaurant), SecurityUtil.get().getUserTo());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/user")
    public UserTo getAuthUser() {
        return SecurityUtil.get().getUserTo();
    }

    @PostMapping(value = "/user/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUserWithLocation(@RequestBody User user) {
        User created = userService.save(creatorUtil.get(User.class, user));//super.saveUser(creatorUtil.get(User.class, user));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    private List<Restaurant> getRestaurants(LocalDateTime startDate, LocalDateTime endDate, List<Restaurant> restaurantList) {
        Map<Integer, Integer> voteCount = voteService.getRestaurantsCount(startDate, endDate);
        restaurantList.forEach(restaurant ->
                restaurant.setVoteCount(voteCount.getOrDefault(restaurant.getId(), 0)));
        return restaurantList;
    }

}
