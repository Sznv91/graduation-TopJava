package ru.topjava.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "DATE_TIME")
    LocalDateTime date;

    public Vote() {
        setCurrentDateTime(null);
    }

    public Vote(Restaurant restaurant, User user) {
        this.restaurant = restaurant;
        this.user = user;
        setCurrentDateTime(null);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void setCurrentDateTime(LocalDateTime dateTime){
        LocalDateTime localDateTime;
        localDateTime = Objects.requireNonNullElseGet(dateTime, LocalDateTime::now);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        date = LocalDateTime.parse(localDateTime.format(formatter), formatter);
    }
}
