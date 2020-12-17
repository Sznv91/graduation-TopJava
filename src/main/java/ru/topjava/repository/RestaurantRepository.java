package ru.topjava.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.topjava.entity.AbstractBaseEntity;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.utils.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepository {

    private RestaurantCrudRepository repository;

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
        List<Object[]> todayMenuObj = em.createNativeQuery("SELECT NAME,COST,DATE FROM DISHES WHERE RESTAURANT_ID=:id AND DATE=:current_date ORDER BY NAME ASC").setParameter("current_date", LocalDate.now()).setParameter("id", id).getResultList();

        List<Dish> dishSet = DishMapper(todayMenuObj);
        /*new HashSet<>();
        todayMenuObj.forEach((record) -> {
            String name = (String) record[0];
            double cost = (Double) record[1];
            LocalDate actualDate = ((java.sql.Date) record[2]).toLocalDate();
            dishSet.add(new Dish(name, cost, actualDate));
        });*/
        List<Object[]> restaurant = em.createNativeQuery("SELECT RESTAURANTS.ID,RESTAURANTS.NAME FROM RESTAURANTS WHERE RESTAURANTS.ID=:id").setParameter("id", id).getResultList();
        Restaurant result = new Restaurant();
        restaurant.stream().map(record -> (String) record[1]).forEach(name -> {
            result.setId(id);
            result.setName(name);
            result.setMenu(dishSet);
        });

        if (result.getId() != null) {
            return result;
        }
        throw new NotFoundException("Restaurant id: " + id + " not found in DB");
    }

    public List<Restaurant> getTodayList() {
       /* LocalDate CURRENT_DATE = LocalDate.now();
        List<Restaurant> restaurantList = em.createQuery("SELECT c FROM Restaurant c WHERE c.date=:CURRENT_DATE", Restaurant.class).setParameter("CURRENT_DATE", CURRENT_DATE).getResultList();
//        List<Dish> dishList = List.copyOf(DishMapper(em.createNativeQuery("SELECT * FROM DISHES WHERE DATE =:CURRENT_DATE").setParameter("CURRENT_DATE", CURRENT_DATE).getResultList()));
        List<Dish> dishList = List.copyOf(DishMapper(em.createNativeQuery("SELECT * FROM DISHES WHERE DATE =:CURRENT_DATE").setParameter("CURRENT_DATE", CURRENT_DATE).getResultList()));
        Map<Integer,List<Dish>> dishMap = new HashMap<>();


        Map<Integer, Restaurant> restaurantMap = restaurantList.stream().collect(Collectors.toMap(AbstractBaseEntity::getId, restaurant -> restaurant, (a, b) -> b));
*/
        List<Object[]> joinFromDb = em.createNativeQuery("SELECT RESTAURANTS.ID, RESTAURANTS.NAME, D.RESTAURANT_ID, D.NAME as dname, D.COST, D.DATE as ddate FROM RESTAURANTS LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID WHERE D.DATE=:CURRENT_DATE").setParameter("CURRENT_DATE", LocalDate.now()).getResultList(); //        List<Object[]> joinFromDb = em.createNativeQuery("SELECT RESTAURANTS.ID, RESTAURANTS.NAME, /*RESTAURANTS.DATE,*/ D.RESTAURANT_ID, D.NAME as dname, D.COST, D.DATE as ddate FROM RESTAURANTS LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID WHERE RESTAURANTS.DATE=:CURRENT_DATE AND D.DATE=:CURRENT_DATE").setParameter("CURRENT_DATE", LocalDate.now()).getResultList();

        return restaurantMapper(joinFromDb);//new ArrayList<Restaurant>(restaurantMap.values());//restaurantList;
    }

    public List<Restaurant> getAllHistoryWithDish() {
        List<Object[]> joinFromDb = em.createNativeQuery("SELECT RESTAURANTS.ID, RESTAURANTS.NAME, D.RESTAURANT_ID, D.NAME as dname, D.COST, D.DATE as ddate FROM RESTAURANTS LEFT JOIN DISHES D on RESTAURANTS.ID = D.RESTAURANT_ID").getResultList();

        return restaurantMapper(joinFromDb);
    }

    private List<Dish> DishMapper(List<Object[]> dishes) {
        List<Dish> dishSet = new ArrayList<>();
        dishes.forEach((record) -> {
            String name = (String) record[0];
            double cost = (Double) record[1];
            LocalDate actualDate = ((java.sql.Date) record[2]).toLocalDate();
            dishSet.add(new Dish(name, cost, actualDate));
        });
        return dishSet;
    }

    private List<Restaurant> restaurantMapper(List<Object[]> resultList) {
        Map<Integer, Restaurant> result = new HashMap<>(); // Restaurant_ID & List<Restaurant>
        resultList.stream().forEach((object -> {
            int restaurantId = (Integer) object[0];
            String restaurantName = (String) object[1];
//            LocalDate restaurantDate = ((java.sql.Date) object[2]).toLocalDate();

            int restaurantIdFk = (Integer) object[2];//Integer.parseInt(String.valueOf(object[3]));
            String dishName = (String) object[3];
            Double dishCost = Double.parseDouble(String.valueOf(object[4]));
            LocalDate dishDate = ((java.sql.Date) object[5]).toLocalDate();

            result.putIfAbsent(restaurantId, new Restaurant(restaurantId, restaurantName/*, restaurantDate*/));
            result.get(restaurantId).addDish(new Dish(dishName, dishCost, dishDate));

        }));

        return new ArrayList<>(result.values());
    }
}
