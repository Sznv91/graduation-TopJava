package ru.topjava.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "dishes")
//, uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id"}, name = "DISHES_RESTAURANTS_ID_fk")})
public class Dish extends AbstractNamedEntity {


    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID", nullable = false)
    Restaurant restaurant;

    @Column(name = "COST")
    private double cost;

    @Column(name = "DATE")
    @Embedded
    @Type(type = "java.time.LocalDate")
    private LocalDate date;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

//    @Embedded
//    @Type(type = "java.time.LocalDate")
    @Transient
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Dish() {
    }

    public Dish(String name, double cost, LocalDate date) {
        this(name, cost, date, null);
    }

    public Dish(String name, double cost) {
        this(name, cost, LocalDate.now(), null);
    }

    public Dish(String name, double cost, LocalDate date, Restaurant restaurant) {
        super(null, name);
        this.cost = cost;
        this.date = date;
        this.restaurant = restaurant;
    }

}
