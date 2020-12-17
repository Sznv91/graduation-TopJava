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
        List<Object[]> todayMenuObj = em.createNativeQuery(
                "SELECT NAME,COST,DATE " +
                        "FROM DISHES " +
                        "WHERE RESTAURANT_ID=:id AND DATE=:current_date " +
                        "ORDER BY NAME ASC")
                .setParameter("current_date", LocalDate.now()).setParameter("id", id).getResultList();

        List<Dish> dishList = new ArrayList<>();
        new HashSet<>();
        todayMenuObj.forEach((record) -> {
            String name = (String) record[0];
            double cost = (Double) record[1];
            LocalDate actualDate = ((java.sql.Date) record[2]).toLocalDate();
            dishList.add(new Dish(name, cost, actualDate));
        });
        List<Object[]> restaurant = em.createNativeQuery(
                "SELECT RESTAURANTS.ID,RESTAURANTS.NAME " +
                        "FROM RESTAURANTS " +
                        "WHERE RESTAURANTS.ID=:id")
                .setParameter("id", id).getResultList();
        Restaurant result = new Restaurant();
        restaurant.stream().map(record -> (String) record[1]).forEach(name -> {
            result.setId(id);
            result.setName(name);
            result.setMenu(dishList);
        });

        if (result.getId() != null) {
            return result;
        }
        throw new NotFoundException("Restaurant id: " + id + " not found in DB");
    }

    public List<Restaurant> getTodayList() {
        List<Object[]> joinFromDb = em.createNativeQuery(
                "SELECT RESTAURANTS.ID, RESTAURANTS.NAME, D.NAME as dname, D.COST, D.DATE as ddate " +
                        "FROM RESTAURANTS " +
                        "LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID " +
                        "WHERE D.DATE=:CURRENT_DATE")
                .setParameter("CURRENT_DATE", LocalDate.now()).getResultList();

        return restaurantMapper(joinFromDb);
    }

    public List<Restaurant> getAllHistoryWithDish() {
        List<Object[]> joinFromDb = em.createNativeQuery(
                "SELECT RESTAURANTS.ID, RESTAURANTS.NAME, D.NAME as dname, D.COST, D.DATE as ddate " +
                        "FROM RESTAURANTS " +
                        "LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID")
                .getResultList();

        return restaurantMapper(joinFromDb);
    }

    private List<Restaurant> restaurantMapper(List<Object[]> resultList) {
        Map<Integer, Restaurant> result = new HashMap<>();
        resultList.forEach((object -> {
            int restaurantId = (Integer) object[0];
            String restaurantName = (String) object[1];

            String dishName = (String) object[2];
            double dishCost = Double.parseDouble(String.valueOf(object[3]));
            LocalDate dishDate = ((java.sql.Date) object[4]).toLocalDate();

            result.putIfAbsent(restaurantId, new Restaurant(restaurantId, restaurantName));
            result.get(restaurantId).addDish(new Dish(dishName, dishCost, dishDate));
        }));
        return new ArrayList<>(result.values());
    }
}
