package ru.topjava.service;

import ru.topjava.entity.Vote;

public class VoteTestData {

    public static int EXIST_VOTE_ID = 100005;
    public static int NOT_EXIST_VOTE_ID = -1;

    public static int newVoteId = 100007;

    public static Vote existVoteWithoutId = new Vote(RestaurantTestData.restaurantWithTodayMenu, UserTestData.admin);

    public static Vote updatedVoteWithoutId = new Vote(RestaurantTestData.anotherRestaurantWithTodayMenu, UserTestData.admin);

    public static Vote newVote = new Vote(RestaurantTestData.restaurantWithTodayMenu, UserTestData.user);


}
