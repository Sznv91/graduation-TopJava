package ru.topjava.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Dish> menu = new HashSet<>();

    public void setMenu(Collection<Dish> menu) {
        this.menu = Set.copyOf(menu);
    }

    public Set<Dish> getMenu() {
        return menu;
    }

}
