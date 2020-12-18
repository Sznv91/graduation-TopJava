package ru.topjava.main;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.repository.RestaurantRepository;
import ru.topjava.repository.UserCrudRepository;
import ru.topjava.repository.VoteRepository;
import ru.topjava.service.RestaurantService;
import ru.topjava.service.UserService;
import ru.topjava.utils.NotFoundException;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

//
//        UserCrudRepository repository = ctx.getBean(UserCrudRepository.class);
//        UserCrudRepository customRepo = ctx.getBean(UserCrudRepository.class);

        UserService userService = ctx.getBean(UserService.class);
        RestaurantService restaurantService = ctx.getBean(RestaurantService.class);
        VoteRepository voteRepository = ctx.getBean(VoteRepository.class);

        User user = userService.getUser(100000);
        Restaurant restaurant = restaurantService.getOneWithTodayMenu(100003);
        Restaurant restaurant1 = restaurantService.getOneWithTodayMenu(100002);

        System.out.println(user.getName() + " User Name from service. " + restaurant.getName() + " Restaurant name");

        Vote firstVote = new Vote(LocalDateTime.now(), restaurant, user);
        Vote secondVote = new Vote(LocalDateTime.of(2020, 12,18, 10,9),restaurant1,user);
        Vote thirdVote = new Vote(LocalDateTime.of(2020,12,17,15,14), restaurant1,user);
        voteRepository.save(firstVote);
        voteRepository.save(secondVote);
        voteRepository.save(thirdVote);

        Vote voteFromDb = voteRepository.get(100005);
        System.out.println(voteFromDb.getUser().getId() + " UserId from entity Vote from DB");

        ctx.close();


    }

}
