package ru.topjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.topjava.entity.Restaurant;

public interface RestaurantCrudRepository extends JpaRepository<Restaurant, Integer> {

}
