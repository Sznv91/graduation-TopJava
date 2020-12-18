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
import ru.topjava.repository.UserRepository;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.utils.NotFoundException;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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


        UserRepository userRepository = ctx.getBean(UserRepository.class);
        UserService userService = ctx.getBean(UserService.class);

        RestaurantRepository restaurantRepository = ctx.getBean(RestaurantRepository.class);
        RestaurantService restaurantService = ctx.getBean(RestaurantService.class);

        Restaurant fistRestaurant = new Restaurant("first test restaurant", new Dish("fist dish test restaurant" , 11.02), new Dish("Second dish test resr", 12.02));
//        fistRestaurant.addDish(new Dish("firstDish test restaurent", 11.01, fistRestaurant));

        restaurantService.create(fistRestaurant, 100001);

        ctx.close();


    }

}
