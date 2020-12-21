package ru.topjava.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.topjava.entity.Restaurant;
import ru.topjava.utils.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.*;

@Repository
public class RestaurantRepository {

    private final RestaurantCrudRepository repository;

    @PersistenceContext
    EntityManager em;

    public RestaurantRepository(RestaurantCrudRepository repository) {
        this.repository = repository;
    }

    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public Restaurant getOneWithHistoryDish(int id) { //Get with ALL DATE
        return repository.findById(id).orElse(null);
    }

    public Restaurant getOneWithCurrentDate(@Param("id") int id) {
        Restaurant restaurant;
        try {
            restaurant = em.createQuery(
                    "SELECT r " +
                            "FROM Restaurant r " +
                            "JOIN FETCH r.menu d " +
                            "WHERE r.id=:id " +
                            "AND d.date=:current_date", Restaurant.class)
                    .setParameter("id", id)
                    .setParameter("current_date", LocalDate.now())
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new NotFoundException("Restaurant id: " + id + " not found in DB");
        }
        return restaurant;
    }

    public List<Restaurant> getTodayList() {

        return em.createQuery(
                "SELECT DISTINCT r " +
                        "FROM Restaurant r " +
                        "JOIN FETCH r.menu d " +
                        "WHERE d.date=:current_date " +
                        "AND r.enable=true ", Restaurant.class)
                .setParameter("current_date", LocalDate.now())
                .getResultList();
    }

    public List<Restaurant> getAllHistoryWithDish() {
        return em.createQuery(
                "SELECT r " +
                        "FROM Restaurant r", Restaurant.class)
                .getResultList();
    }

    public Restaurant getReference(int id) {
        return em.getReference(Restaurant.class, id);
    }

    /*private List<Restaurant> restaurantMapper(List<Object[]> resultList) {
        Map<Integer, Restaurant> result = new HashMap<>();
        resultList.forEach((object -> {
            int restaurantId = (Integer) object[0];
            String restaurantName = (String) object[1];
            Boolean enable = (Boolean) object[2];

            String dishName = (String) object[3];
            double dishCost = Double.parseDouble(String.valueOf(object[4]));
            LocalDate dishDate = ((java.sql.Date) object[5]).toLocalDate();

            result.putIfAbsent(restaurantId, new Restaurant(restaurantId, restaurantName, enable));
            result.get(restaurantId).addDish(new Dish(dishName, dishCost, dishDate));
        }));
        return new ArrayList<>(result.values());
    }*/
}
