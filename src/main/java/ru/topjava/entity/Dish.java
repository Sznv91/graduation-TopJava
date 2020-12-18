package ru.topjava.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;


@Embeddable
@Table(name = "dishes")
//, uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id"}, name = "DISHES_RESTAURANTS_ID_fk")})
public class Dish {


    private double cost;

    private String name;

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

    public Dish() {
    }

    public Dish(String name, double cost) {
        this.name = name;
        this.cost = cost;
        this.date = LocalDate.now();
    }

    public Dish(String name, double cost, LocalDate date){
        this(name,cost);
        this.date = date;
    }

}
