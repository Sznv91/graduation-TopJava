package ru.topjava.main;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.repository.RestaurantRepository;
import ru.topjava.repository.UserCrudRepository;
import ru.topjava.service.UserService;
import ru.topjava.utils.NotFoundException;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@Transactional
public class Start {

    public static void main(String... args) throws SQLException, IOException {
//        ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(GraduationJpaConfig.class);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        ctx.register(GraduationJpaConfig.class);
        ctx.refresh();

                            /*INIT DB*/
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        Connection connection = dataSource.getConnection();
        ScriptRunner runner = new ScriptRunner(connection, false, true);
        Reader populate = Resources.getResourceAsReader("populateDB.sql");
        Reader dbInit = Resources.getResourceAsReader("initDB_H2.sql");
        runner.runScript(dbInit);
        runner.runScript(populate);


        UserCrudRepository repository = ctx.getBean(UserCrudRepository.class);
        UserCrudRepository customRepo = ctx.getBean(UserCrudRepository.class);

//        User fromDb = repository.getOne(100000);

//        System.out.println(fromDb.getName());

        //User toDB = new User(3,"Test User");
        //User fromSave = repository.save(toDB);
        //System.out.println(fromSave.getName() + "From DB");

//        User fromCustomRepo = customRepo.getOne(100000);
//        System.out.println(fromCustomRepo.getName() + " From custom ID");

        UserService userService = ctx.getBean(UserService.class);

        User fromService = userService.getUser(100000);
        System.out.println(fromService.getRoles() + " User from Service");
        try {
            User fromServiceNotFound = userService.getUser(10000);
            System.out.println("Except Runtime Exception");
        } catch (NotFoundException e) {

        }




        RestaurantRepository restaurantRepository = ctx.getBean(RestaurantRepository.class);

        Restaurant restaurant = new Restaurant();
//        restaurant.setId(100002);
        restaurant.setName("First rest");
        ArrayList<Dish> dishList = new ArrayList<>();
        Dish dish = new Dish("dish", 12.3);
        Dish dish2 = new Dish("New Dish", 14.88);
        /*dish.setRestaurant(restaurant);
        dish2.setRestaurant(restaurant);*/
//        dish.setId(100003);
        dishList.add(dish);
        dishList.add(dish2);
        restaurant.setMenu(dishList);
        System.out.println(restaurant.getMenu() + " without Db");


        restaurantRepository.save(restaurant);

        Restaurant fromDb = restaurantRepository.get(100002);
        System.out.println(fromDb.getName() + " From DB");
        System.out.println(fromDb.getMenu());


        ctx.close();


    }

}
