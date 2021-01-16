package ru.topjava.controller;

import org.springframework.stereotype.Controller;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.service.VoteService;
import ru.topjava.to.UserTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
public class ApplicationController {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final VoteService voteService;

    public ApplicationController(UserService userService, RestaurantService restaurantService, VoteService voteService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.voteService = voteService;
    }

    public User saveUser(User user) {
        return userService.save(user);
    }

    public User getUser(int id) {
        return userService.get(id);
    }

    public Restaurant saveRestaurant(Restaurant restaurant, UserTo admin) {
        return restaurantService.create(restaurant, admin);
    }

    public Restaurant updateRestaurant(Restaurant restaurant, UserTo admin) {
        return restaurantService.update(restaurant, admin);
    }

    public Restaurant addDishes(int restaurantId, List<Dish> dishes, UserTo admin) {
        return restaurantService.addDish(restaurantId, dishes, admin);
    }

    public Restaurant getOneRestaurantWithTodayMenu(int restaurantId) {
        return restaurantService.getOneWithTodayMenu(restaurantId);
    }

    public Restaurant getOneRestaurantWithHistoryMenu(int restaurantId) {
        return restaurantService.getOneWithHistoryMenu(restaurantId);
    }

    public List<Restaurant> getRestaurantsWithTodayMenu() {
        return getRestaurants(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999),
                restaurantService.getAllWithTodayMenu());
    }

    public List<Restaurant> getRestaurantsWithHistory(LocalDate start, LocalDate end) {
        return getRestaurants(LocalDateTime.of(start, LocalTime.of(0,0,0,0))
                , LocalDateTime.of(end, LocalTime.of(23,59,59,999999999))
                , restaurantService.getAllWithHistoryMenu(start, end));
    }

    private List<Restaurant> getRestaurants(LocalDateTime startDate, LocalDateTime endDate, List<Restaurant> restaurantList) {
        Map<Integer, Integer> voteCount = voteService.getRestaurantsCount(startDate, endDate);
        restaurantList.forEach(restaurant ->
                restaurant.setVoteCount(voteCount.getOrDefault(restaurant.getId(), 0)));
        return restaurantList;
    }

    public Vote saveVote(int restaurantId, int userId) {
        Restaurant restaurant = restaurantService.getReference(restaurantId);
        User user = userService.get(userId);
        return voteService.save(new Vote(restaurant, user));
    }

    public Boolean votedToday(int userId) {
        return voteService.voteToday(userService.getReference(userId)) > 0;
    }

}
