package ru.topjava.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.topjava.entity.Restaurant;
import ru.topjava.utils.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RestaurantRepository {

    private final RestaurantCrudRepository repository;

    @PersistenceContext
    EntityManager em;

    public RestaurantRepository(RestaurantCrudRepository repository) {
        this.repository = repository;
    }

    public Restaurant save(Restaurant restaurant) {
        return new Restaurant(repository.save(restaurant));
    }

    public Restaurant getOneWithHistoryDish(int id) { //Get with ALL DATE
        return repository.findById(id).orElse(null);
    }

    public Restaurant getOneWithCurrentDate(@Param("id") int id) {
        Restaurant restaurant;
        Long voteCount;
        try {
            restaurant = em.createQuery(
                    "SELECT r " +
                            "FROM Restaurant r " +
                            "JOIN FETCH r.menu d " +
                            "WHERE r.id = :id " +
                            "AND d.date = :current_date", Restaurant.class)
                    .setParameter("id", id)
                    .setParameter("current_date", LocalDate.now())
                    .getSingleResult();
            voteCount = em.createQuery(
                    "SELECT count (c) " +
                            "FROM Vote c " +
                            "WHERE c.restaurant = :restaurant " +
                            "AND c.date BETWEEN :start_day AND :end_day", Long.class)
                    .setParameter("restaurant", restaurant)
                    .setParameter("start_day", LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0))
                    .setParameter("end_day", LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999))
                    .getSingleResult();
            restaurant.setVoteCount(voteCount.intValue());
        } catch (javax.persistence.NoResultException e) {
            throw new NotFoundException("Restaurant id: " + id + " not found in DB");
        }
        return new Restaurant(restaurant);
    }

    public List<Restaurant> getTodayList() {
        return em.createQuery(
                "SELECT DISTINCT r " +
                        "FROM Restaurant r " +
                        "JOIN FETCH r.menu d " +
                        "WHERE d.date = :current_date " +
                        "AND r.enable = true ", Restaurant.class)
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

}
