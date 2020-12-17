package ru.topjava.entity;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "DISHES", joinColumns = @JoinColumn(name = "RESTAURANT_ID"))
    private List<Dish> menu;

    public void setMenu(Collection<Dish> menu) {
        this.menu = new ArrayList<>(List.copyOf(menu));
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void addDish(Dish dishes) {
        if (menu == null) {
            menu = new ArrayList<>();
        }
        menu.add(dishes);
    }

    public Restaurant() {

    }

    public Restaurant(int id, String name) {
        super(id, name);
    }

    public Restaurant(String name, Dish... dishes) {
        this(null, name, dishes);
    }

    public Restaurant(Integer id, String name, Dish... dishes) {
        super(id, name);
        menu = new ArrayList<>(List.copyOf(Arrays.asList(dishes)));
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getMenu().toArray(new Dish[0]));
    }
}
