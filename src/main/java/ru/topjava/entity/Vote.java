package ru.topjava.entity;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "VOTES")
public class Vote extends AbstractBaseEntity {

    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;

    @OneToOne (fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Vote() {
    }

    public Vote(LocalDateTime dateTime, Restaurant restaurant, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateTime = LocalDateTime.parse(dateTime.format(formatter),formatter);
        this.restaurant = restaurant;
        this.user = user;
    }


    /*------------------------------------------*/

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
}
