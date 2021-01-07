package ru.topjava.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.topjava.entity.Restaurant;
import ru.topjava.utils.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EnableCaching
@Repository
public class RestaurantRepository {

    @PersistenceContext
    EntityManager em;

    public Restaurant save(Restaurant restaurant) {
        em.persist(restaurant);
        return restaurant;
    }

    public Restaurant update(Restaurant restaurant) {
        return em.merge(restaurant);
    }

    public Restaurant getOneWithHistoryDish(int id) { //Get with ALL DATE
        return em.find(Restaurant.class, id);
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

    @Cacheable(value = "RestaurantCache", cacheManager = "RestaurantCache")
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
