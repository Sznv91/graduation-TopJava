package ru.topjava.entity;

import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.time.LocalDate;


@Embeddable
@Table(name = "dishes")
//, uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id"}, name = "DISHES_RESTAURANTS_ID_fk")})
public class Dish {

    private double cost;

    private String name;

    private LocalDate date;

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

    /*Just for test*/
    @Override
    public String toString() {
        return "Dish{" +
                "cost=" + cost +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
