package ru.topjava.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.topjava.entity.Dish;
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

        List<Object[]> joinFromDb = em.createNativeQuery(
                "SELECT RESTAURANTS.ID, RESTAURANTS.NAME, RESTAURANTS.ENABLE, D.NAME as dname, D.COST, D.DATE as ddate " +
                        "FROM RESTAURANTS " +
                        "LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID " +
                        "WHERE D.DATE=:CURRENT_DATE " +
                        "AND RESTAURANTS.ID =:id")
                .setParameter("CURRENT_DATE", LocalDate.now())
                .setParameter("id", id)
                .getResultList();

        if (joinFromDb.size() > 0) {
            return restaurantMapper(joinFromDb).get(0);
        }
        throw new NotFoundException("Restaurant id: " + id + " not found in DB");
    }

    public List<Restaurant> getTodayList() {
        List<Object[]> joinFromDb = em.createNativeQuery(
                "SELECT RESTAURANTS.ID, RESTAURANTS.NAME, RESTAURANTS.ENABLE, D.NAME as dname, D.COST, D.DATE as ddate " +
                        "FROM RESTAURANTS " +
                        "LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID " +
                        "WHERE D.DATE=:CURRENT_DATE " +
                        "AND RESTAURANTS.ENABLE = TRUE")
                .setParameter("CURRENT_DATE", LocalDate.now()).getResultList();

        return restaurantMapper(joinFromDb);
    }

    public List<Restaurant> getAllHistoryWithDish() {
        List<Object[]> joinFromDb = em.createNativeQuery(
                "SELECT RESTAURANTS.ID, RESTAURANTS.NAME, RESTAURANTS.ENABLE, D.NAME as dname, D.COST, D.DATE as ddate " +
                        "FROM RESTAURANTS " +
                        "LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID")
                .getResultList();

        return restaurantMapper(joinFromDb);
    }

    public Restaurant getReference (int id){
        return em.getReference(Restaurant.class, id);
    }

    private List<Restaurant> restaurantMapper(List<Object[]> resultList) {
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
    }
}
