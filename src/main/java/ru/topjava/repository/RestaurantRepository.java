package ru.topjava.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.utils.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
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

        /*List<Dish> dishList = new ArrayList<>();
        new HashSet<>();
        todayMenuObj.forEach((record) -> {
            String name = (String) record[0];
            double cost = (Double) record[1];
            LocalDate actualDate = ((java.sql.Date) record[2]).toLocalDate();
            dishList.add(new Dish(name, cost, actualDate));
        });*/
        List<Object[]> restaurant = em.createNativeQuery(
                "SELECT RESTAURANTS.ID,RESTAURANTS.NAME, RESTAURANTS.ENABLE " +
                        "FROM RESTAURANTS " +
                        "WHERE RESTAURANTS.ID=:id")
                .setParameter("id", id).getResultList();
        Restaurant result = new Restaurant();
        restaurant.forEach(record -> {
            String name = (String) record[1];
            boolean enable = (boolean) record[2];
            result.setId(id);
            result.setName(name);
            result.setEnable(enable);
//            result.setMenu(dishList);
        });

        if (result.getId() != null) {
            return result;
        }
        throw new NotFoundException("Restaurant id: " + id + " not found in DB");
    }

    public List<Restaurant> getTodayList() {
/*

        CriteriaQuery<Restaurant> restaurantCriteria = em.getCriteriaBuilder().createQuery(Restaurant.class);
        Root<Restaurant> restaurantRoot = restaurantCriteria.from(Restaurant.class);
        Join<Restaurant, Dish> dish = restaurantRoot.join("menu");
        restaurantCriteria.select(restaurantRoot);
        restaurantCriteria.where(em.getCriteriaBuilder().equal(dish.get("date"),LocalDate.now()));
        List<Restaurant> result = em.createQuery(restaurantCriteria).getResultList();
*/

        List<Restaurant> result = em.createQuery("SELECT d.restaurant FROM Dish d JOIN Restaurant r WHERE d.date='2020-12-18' AND r.enable=true ", Restaurant.class).getResultList();

//        List<Restaurant> result = em.createQuery("SELECT r FROM Restaurant AS r, IN (r.menu) AS d WHERE d.date=:currentdate AND r.enable=true", Restaurant.class).setParameter("currentdate", LocalDate.now()).getResultList();


       /* List<Restaurant> result = em.createQuery("SELECT r FROM Restaurant r LEFT JOIN Dish d WHERE d.date=:currentdate", Restaurant.class).setParameter("currentdate", LocalDate.now()).getResultList();
        List<Object[]> joinFromDb = em.createNativeQuery(
                "SELECT RESTAURANTS.ID, RESTAURANTS.NAME, RESTAURANTS.ENABLE, D.NAME as dname, D.COST, D.DATE as ddate " +
                        "FROM RESTAURANTS " +
                        "LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID " +
                        "WHERE D.DATE=:CURRENT_DATE " +
                        "AND RESTAURANTS.ENABLE = TRUE")
                .setParameter("CURRENT_DATE", LocalDate.now()).getResultList();
*/
//        return restaurantMapper(joinFromDb);
        return result;
    }

    public List<Restaurant> getAllHistoryWithDish() {
        List<Restaurant> result = em.createQuery("SELECT r FROM Restaurant r", Restaurant.class).getResultList();
        /*List<Object[]> joinFromDb = em.createNativeQuery(
                "SELECT RESTAURANTS.ID, RESTAURANTS.NAME, RESTAURANTS.ENABLE, D.NAME as dname, D.COST, D.DATE as ddate " +
                        "FROM RESTAURANTS " +
                        "LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID")
                .getResultList();

        return restaurantMapper(joinFromDb);*/
        return result;
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
//            result.get(restaurantId).addDish(new Dish(dishName, dishCost, dishDate));
        }));
        return new ArrayList<>(result.values());
    }
}
