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
    private List<Dish> menu;// = new HashSet<>();

    /*@Column(name = "DATE")
    private LocalDate date = LocalDate.now();*/

    public void setMenu(Collection<Dish> menu) {
        this.menu = new ArrayList<>(List.copyOf(menu));//List.copyOf(menu);//List.copyOf(menu);
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void addDish(Dish dishes) {
        if (menu == null) {
            menu = new ArrayList<>();
        }
        menu.add(dishes);
//        menu.addAll(CollectionUtils.arrayToList(dishes));
        /*for (Dish dish : dishes) {
            menu.add(new Dish(dish.getName(), dish.getCost()));
        }*/
    }

   /* public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }*/

    public Restaurant() {

    }

    public Restaurant(int id, String name) {
        super(id, name);
    }

    /*public Restaurant(int id, String name, LocalDate date) {
        this(id, name);
        this.date = date;
    }*/

    /*public Restaurant(String name) {
        this.name = name;
    }*/

    public Restaurant(String name, Dish... dishes) {
        super(null, name);//this(name);
        menu = new ArrayList<>(List.copyOf(menu));
    }

    public Restaurant(Integer id, String name, Dish... dishes) {
        super(id, name);
        menu = new ArrayList<Dish>(Arrays.asList(dishes));
    }

    public Restaurant(Restaurant restaurant) {
        super(restaurant.getId(), restaurant.getName());
        menu = new ArrayList<>(restaurant.getMenu());
//        Collections.copy(menu, restaurant.getMenu());
    }
}
