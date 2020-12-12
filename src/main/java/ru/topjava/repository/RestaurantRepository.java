package ru.topjava.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.Restaurant;

@Repository
public class RestaurantRepository {

    private RestaurantCrudRepository repository;

    public RestaurantRepository(RestaurantCrudRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void save(Restaurant restaurant){

        repository.save(restaurant);
    }

    public Restaurant get (int id){
        return repository.findById(id).orElse(null);
    }
}
