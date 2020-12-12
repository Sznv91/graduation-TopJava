package ru.topjava.entity;

import javax.persistence.Embeddable;
import javax.persistence.Table;


//@Entity
@Embeddable
@Table(name = "dishes")
//, uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id"}, name = "DISHES_RESTAURANTS_ID_fk")})
public class Dish {//extends AbstractNamedEntity {
/*

    @Id
    private int id;
*/

//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//Lazy
//    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "restaurant_id")
//    private Restaurant restaurant;
/*

    @Column(name = "NAME")
    private String name;
*/

    //    @Column(name = "cost")
    private double cost;

    private String name;

    public Dish() {
    }

    public Dish(String name, double cost) {
//        this.id = null;
        this.name = name;
        this.cost = cost;
    }

//    public Restaurant getRestaurant() {
//        return restaurant;
//    }

//    public void setRestaurant(Restaurant restaurant) {
//        this.restaurant = restaurant;
//    }
}
