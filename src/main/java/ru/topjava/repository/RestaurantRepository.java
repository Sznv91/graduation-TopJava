package ru.topjava.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RestaurantRepository {

    private RestaurantCrudRepository repository;

    @PersistenceContext
    EntityManager em;

    public RestaurantRepository(RestaurantCrudRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void save(Restaurant restaurant) {

        repository.save(restaurant);
    }

    public Restaurant get(int id) { //Get with ALL DATE
        return repository.findById(id).orElse(null);
    }

    public Restaurant getCurrentDate(@Param("id") int id) {
        em.createQuery("SELECT r FROM Restaurant r WHERE r.id=:id AND Restaurant.menu ") //Hibernate can't LEFT JOIN
        return null;
    }
}
