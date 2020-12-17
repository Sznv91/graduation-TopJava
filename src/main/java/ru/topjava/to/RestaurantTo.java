package ru.topjava.to;

import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantTo {

    Integer id;
    String name;
    List<DishTo> menu;

    private RestaurantTo(int id, String name, List<Dish> menu) {
        this.id = id;
        this.name = name;
        this.menu = new ArrayList<>();
        menu.forEach(dish -> this.menu.add(DishTo.getDishTo(dish)));
    }

    public static RestaurantTo getRestaurantTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getMenu());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTo that = (RestaurantTo) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                Objects.equals(menu, that.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, menu);
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                '}';
    }

    private static class DishTo {
        private final double cost;

        private final String name;

        private final LocalDate date;

        private DishTo(String name, double cost, LocalDate date){
            this.name = name;
            this.cost = cost;
            this.date = date;
        }

        public static DishTo getDishTo(Dish dish){
            return new DishTo(dish.getName(), dish.getCost(), dish.getDate());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DishTo dishTo = (DishTo) o;
            return Double.compare(dishTo.cost, cost) == 0 &&
                    name.equals(dishTo.name) &&
                    date.equals(dishTo.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cost, name, date);
        }

        @Override
        public String toString() {
            return "DishTo{" +
                    "cost=" + cost +
                    ", name='" + name + '\'' +
                    ", date=" + date +
                    '}';
        }
    }

}
