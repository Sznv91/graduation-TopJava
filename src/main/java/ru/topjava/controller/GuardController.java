package ru.topjava.controller;

import org.springframework.stereotype.Controller;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.service.VoteService;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class GuardController {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final VoteService voteService;

    public GuardController(UserService userService, RestaurantService restaurantService, VoteService voteService) {
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

    public Restaurant saveRestaurant(Restaurant restaurant, int userId) {
        User user = userService.get(userId);
        return restaurantService.create(restaurant, user);
    }

    public Restaurant getRestaurantWithTodayMenu(int restaurantId) {
        return restaurantService.getOneWithTodayMenu(restaurantId);
    }

    public Restaurant getRestaurantWithHistoryMenu(int restaurantId) {
        return restaurantService.getOneWithHistoryMenu(restaurantId);
    }

    public List<Restaurant> getRestaurantsWithTodayMenu() {
        LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999);
        List<Restaurant> restaurantList = restaurantService.getAllWithTodayMenu();
        Map<Integer, Integer> voteCount = voteService.getRestaurantsCount(startDate,endDate);
        restaurantList.forEach(restaurant ->
                restaurant.setVoteCount(voteCount.getOrDefault(restaurant.getId(), 0)));
        return restaurantList;
    }

    public List<Restaurant> getRestaurantsWithHistory() { //todo make test
        List<Restaurant> restaurantList = restaurantService.getAllWithHistoryMenu();
        LocalDateTime startDate = LocalDateTime.MIN;
        LocalDateTime endDate = LocalDateTime.MAX;
        Map<Integer, Integer> voteCount = voteService.getRestaurantsCount(startDate,endDate);
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
