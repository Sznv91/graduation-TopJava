package ru.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.service.UserTestData;
import ru.topjava.utils.TestUtil;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {GraduationJpaConfig.class, TestConfig.class})

@Transactional
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
        perform(MockMvcRequestBuilders.get("/restaurants/100002").with(TestUtil.userAuth(UserTestData.user)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"id\":100002,\"name\":\"first restaurant\",\"menu\":[{\"cost\":1.01,\"name\":\"first dish First restaurant\",\"date\":[2021,1,8]},{\"cost\":2.1,\"name\":\"second dish First restaurant\",\"date\":[2021,1,8]}],\"enable\":true,\"voteCount\":1}"))
        ;
    }

    @Test
    void createRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.post("/restaurants/create").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"New Restaurant\"}").accept(MediaType.APPLICATION_JSON).with(TestUtil.userAuth(UserTestData.admin)))
                .andExpect(status().isCreated())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"id\":100007,\"name\":\"New Restaurant\",\"menu\":[],\"enable\":true,\"voteCount\":0}"))
        ;
    }

    @Test
    void addDish() throws Exception {
        perform(MockMvcRequestBuilders.post("/restaurants/100002/add_dishes").contentType(MediaType.APPLICATION_JSON).content("[{\"cost\":10.01,\"name\":\"first dish First restaurant123\"},{\"cost\":22.1,\"name\":\"second dish First restauran321t\"}]").accept(MediaType.APPLICATION_JSON).with(TestUtil.userAuth(UserTestData.admin)))
                .andExpect(status().isCreated())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("{\"id\":100002,\"name\":\"first restaurant\",\"menu\":[{\"cost\":1.01,\"name\":\"first dish First restaurant\",\"date\":[2021,1,8]},{\"cost\":2.1,\"name\":\"second dish First restaurant\",\"date\":[2021,1,8]},{\"cost\":3.11,\"name\":\"Late Dish First restaurant\",\"date\":[2020,10,20]},{\"cost\":10.01,\"name\":\"first dish First restaurant123\",\"date\":[2021,1,8]},{\"cost\":22.1,\"name\":\"second dish First restauran321t\",\"date\":[2021,1,8]}],\"enable\":true,\"voteCount\":0}"))
        ;
    }
}