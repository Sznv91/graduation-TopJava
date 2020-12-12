package ru.topjava.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RestaurantRepository {

    private RestaurantCrudRepository repository;

    @PersistenceContext
    EntityManager em;

    public RestaurantRepository(RestaurantCrudRepository repository) {
        this.repository = repository;
    }

    public void save(Restaurant restaurant) {
        repository.save(restaurant);
    }

    public Restaurant getOneWithHistoryDish(int id) { //Get with ALL DATE
        return repository.findById(id).orElse(null);
    }

    public Restaurant getOneWithCurrentDate(@Param("id") int id) {
        List<Object[]> todayMenuObj = em.createNativeQuery("SELECT NAME,COST,DATE FROM DISHES WHERE RESTAURANT_ID=:id AND DATE=:current_date").setParameter("current_date", LocalDate.now()).setParameter("id", id).getResultList();

        Set<Dish> dishSet = new HashSet<>();
        todayMenuObj.forEach((record) -> {
            String name = (String) record[0];
            double cost = (Double) record[1];
            LocalDate actualDate = ((java.sql.Date) record[2]).toLocalDate();
            dishSet.add(new Dish(name, cost, actualDate));
        });
        List<Object[]> restaurant = em.createNativeQuery("SELECT RESTAURANTS.ID,RESTAURANTS.NAME FROM RESTAURANTS WHERE RESTAURANTS.ID=:id").setParameter("id", id).getResultList();
        Restaurant result = new Restaurant();
        restaurant.stream().map(record -> (String) record[1]).forEach(name -> {
            result.setId(id);
            result.setName(name);
            result.setMenu(dishSet);
        });
        return result;
    }
}
