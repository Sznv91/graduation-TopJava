package ru.topjava.entity;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "dishes")
//, uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id"}, name = "DISHES_RESTAURANTS_ID_fk")})
public class Dish extends AbstractNamedEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;


    @Column(name = "NAME")
    private String name;

    @Column(name = "cost")
    private double cost;

    @Column(name = "date")
    private LocalDate date;

    public Dish() {
    }

    public Dish(String name, double cost) {
        this.name = name;
        this.cost = cost;
        if (date == null) {
            date = LocalDate.now();
        }
    }

    public Dish(String name, double cost, LocalDate date) {
        this(name, cost);
        this.date = date;
    }


    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    /*JUST FOR TEST*/

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", date=" + date +
                '}';
    }
}
