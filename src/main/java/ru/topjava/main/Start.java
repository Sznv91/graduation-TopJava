package ru.topjava.main;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.controller.GraduationController;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

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

        /*UserService userService = ctx.getBean(UserService.class);
        RestaurantService restaurantService = ctx.getBean(RestaurantService.class);
        VoteRepository voteRepository = ctx.getBean(VoteRepository.class);

        User user = userService.getUser(100001);
        User admin = userService.getUser(100000);
        Restaurant restaurant = restaurantService.getOneWithTodayMenu(100003);
        Vote firstVote = new Vote(restaurant, user);
        voteRepository.create(firstVote);

        System.out.println("UserID: " + admin.getId() + " " + voteRepository.hasVoteToday(admin) + " Have voice today");
*/

        GraduationController controller = ctx.getBean(GraduationController.class);

        User user = controller.getUser(100001);
        Restaurant restaurant = controller.getOneRestaurantWithTodayMenu(100003);
        Restaurant restaurant2 = controller.getOneRestaurantWithTodayMenu(100002);
        Vote firstVote = controller.saveVote(restaurant.getId(), user.getId());
        System.out.println(controller.votedToday(user.getId()));
        Vote secondVote = controller.saveVote(restaurant2.getId(), user.getId());
        ctx.close();


    }

}
