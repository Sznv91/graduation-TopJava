package ru.topjava.to;

import ru.topjava.entity.Vote;

import java.time.LocalDate;
import java.util.Objects;

public class VoteTo {

    private final Integer id;
    private final int restaurantId;
    private final int userId;
    private final LocalDate date;

    private VoteTo(Vote vote) {
        id = vote.getId();
        restaurantId = vote.getRestaurant().getId();//RestaurantTo.getRestaurantTo(vote.getRestaurant());
        userId = vote.getUser().getId(); //UserTo.getUserTo(vote.getUser());
        date = vote.getDate().toLocalDate();
    }

    public static VoteTo getVoteTo(Vote vote) {
        return new VoteTo(vote);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteTo voteTo = (VoteTo) o;
        return restaurantId == voteTo.restaurantId &&
                userId == voteTo.userId &&
                id.equals(voteTo.id) &&
                date.equals(voteTo.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantId, userId, date);
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                ", date=" + date +
                '}';
    }
}
