package ru.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.topjava.entity.Dish;
import ru.topjava.entity.Restaurant;
import ru.topjava.service.RestaurantTestData;
import ru.topjava.service.UserTestData;
import ru.topjava.utils.AuthUtil;
import ru.topjava.utils.JsonUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {TestConfig.class})

@Transactional
@Sql(scripts = {"classpath:initDB_H2.sql", "classpath:populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
class RestWebControllerTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    @Test
    void getOneRestaurant() throws Exception {
        Restaurant expect = RestaurantTestData.restaurantWithTodayMenu;
        expect.setVoteCount(1);
        perform(MockMvcRequestBuilders.get("/restaurants/100002").with(AuthUtil.userAuth(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonUtil.getJsonString(expect)))
        ;
    }

    @Test
    void createRestaurant() throws Exception {
        Restaurant expect = new Restaurant(100007, "New Restaurant", true);
        expect.setMenu(new ArrayList<Dish>());

        perform(MockMvcRequestBuilders.post("/restaurants").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"New Restaurant\"}").accept(MediaType.APPLICATION_JSON).with(AuthUtil.userAuth(UserTestData.admin)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonUtil.getJsonString(expect)))

        ;
    }

    @Test
    void addDish() throws Exception {
        Restaurant expect = new Restaurant(100002, "first restaurant", true,
                new Dish("first dish First restaurant", 1.01),
                new Dish("second dish First restaurant", 2.1),
                new Dish("Late Dish First restaurant", 3.11, LocalDate.of(2020, 10, 20)),
                new Dish("first dish First restaurant123", 10.01),
                new Dish("second dish First restauran321t", 22.1));
        perform(MockMvcRequestBuilders.put("/restaurants/100002/add_dishes").contentType(MediaType.APPLICATION_JSON).content("[{\"cost\":10.01,\"name\":\"first dish First restaurant123\"},{\"cost\":22.1,\"name\":\"second dish First restauran321t\"}]").accept(MediaType.APPLICATION_JSON).with(AuthUtil.userAuth(UserTestData.admin)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonUtil.getJsonString(expect)));

        ;
    }

    @Test
    void makeVote() throws Exception {
        Restaurant expect = new Restaurant(100003, "second restaurant", true,
                new Dish("first dish Second restaurant", 1.02),
                new Dish("second dish Second restaurant", 2.2));

        perform(MockMvcRequestBuilders.get("/restaurants/100003").with(AuthUtil.userAuth(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonUtil.getJsonString(expect)));

        ;

        expect.setVoteCount(1);

        perform(MockMvcRequestBuilders.post("/restaurants/100003/vote").with(AuthUtil.userAuth(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(JsonUtil.getJsonString(expect)))

        ;

    }
}