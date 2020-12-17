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

    @Column(name = "ENABLE", nullable = false)
    private Boolean enable;

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

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Boolean isEnable() {
        return enable;
    }

    public Restaurant() {

    }

    public Restaurant(int id, String name) {
        super(id, name);
    }

    public Restaurant(int id, String name, boolean enable) {
        super(id, name);
        this.enable = enable;
    }

    public Restaurant(String name, Dish... dishes) {
        this(null, name, true, dishes);
    }

    public Restaurant(Integer id, String name, Boolean isEnable, Dish... dishes) {
        super(id, name);
        menu = new ArrayList<>(List.copyOf(Arrays.asList(dishes)));
        enable = isEnable;
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.isEnable(), restaurant.getMenu().toArray(new Dish[0]));
    }
}
