package ru.topjava.entity;

import javax.persistence.*;


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

    public Dish() {
    }

    public Dish(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
