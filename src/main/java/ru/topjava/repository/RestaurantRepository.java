package ru.topjava.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Set;

@Repository
public class RestaurantRepository {

    @PersistenceContext
    EntityManager em;

    private RestaurantCrudRepository repository;

    public RestaurantRepository(RestaurantCrudRepository repository) {
        this.repository = repository;
    }

    public void save(Restaurant restaurant) { //TODO remake to lazy Initialization on Dish.java
        repository.save(restaurant);
    }

    public Restaurant get(int id) {
        return repository.findById(id).orElse(null);
    }

    public Restaurant getWithTodayMenu(@Param("id") int id) {
        Set<Dish> dishToday = Set.copyOf(em.createQuery("SELECT m FROM Dish m WHERE m.restaurant=:restaurant AND m.date=:current_date", Dish.class).setParameter("restaurant", em.getReference(Restaurant.class,id)).setParameter("current_date", LocalDate.now()).getResultList());
        Restaurant result = em.createQuery("SELECT r FROM Restaurant r WHERE r.id=:restaurant", Restaurant.class).setParameter("restaurant", id).getSingleResult();
        result.setMenu(dishToday);
        return result;
    }
}
