package ru.topjava.controller;

import org.springframework.stereotype.Controller;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.service.VoteService;

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

    public User saveUser (User user){
        return userService.save(user);
    }

    public User getUser(int id){
        return userService.get(id);
    }

    public Restaurant saveRestaurant (Restaurant restaurant, int userId){
        User user = userService.get(userId);
        return restaurantService.create(restaurant, user);
    }

    public Restaurant getRestaurantWithTodayMenu(int restaurantId){
        return restaurantService.getOneWithTodayMenu(restaurantId);
    }

    public Restaurant getRestaurantWithHistoryMenu (int restaurantId){
        return restaurantService.getOneWithHistoryMenu(restaurantId);
    }

    public Vote saveVote (int restaurantId, int userId){
        Restaurant restaurant = restaurantService.getReference(restaurantId);
        User user = userService.get(userId);
        return voteService.save(new Vote(restaurant, user));
    }

    public Boolean votedToday (int userId){
        return voteService.voteToday(userService.getReference(userId)) > 0;
    }
}
