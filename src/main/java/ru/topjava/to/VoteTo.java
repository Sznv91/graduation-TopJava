package ru.topjava.to;

import ru.topjava.entity.Vote;

import java.util.Objects;

public class VoteTo {

    Integer id;
    int restaurantId;
    int userId;

    private VoteTo(Vote vote){
        id = vote.getId();
        restaurantId = vote.getRestaurant().getId();//RestaurantTo.getRestaurantTo(vote.getRestaurant());
        userId = vote.getUser().getId(); //UserTo.getUserTo(vote.getUser());
    }

    public static VoteTo getVoteTo (Vote vote){
        return new VoteTo(vote);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteTo voteTo = (VoteTo) o;
        return restaurantId == voteTo.restaurantId &&
                userId == voteTo.userId &&
                id.equals(voteTo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantId, userId);
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                '}';
    }
}
