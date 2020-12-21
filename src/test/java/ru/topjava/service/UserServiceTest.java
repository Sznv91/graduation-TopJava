package ru.topjava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.User;
import ru.topjava.utils.ExistException;
import ru.topjava.utils.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.topjava.service.UserTestData.*;
import static ru.topjava.to.UserTo.getUserTo;

@SpringJUnitConfig(GraduationJpaConfig.class)
@Sql(scripts = "classpath:populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class UserServiceTest {

    @Autowired
    protected UserService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getById() {
        User actual = service.get(ADMIN_ID);
        assertEquals(getUserTo(admin), getUserTo(actual));

    }

    @Test
    void create() {
        User expected = getNew();//new User(null, "Test user", "test@ya.ru", "123321", Role.USER);
        User actual = service.save(expected);
        expected.setId(actual.getId());
        assertNotNull(actual);
        assertEquals(getUserTo(expected), getUserTo(actual));
    }

    @Test
    void createExistId() {
        assertThrows(ExistException.class, () -> service.save(user));
    }

    @Test
    void Update() {
        User actual = service.update(getUpdated());
        User expected = service.get(USER_ID);
        assertEquals(getUserTo(expected), getUserTo(actual));
    }

    @Test
    void UpdateNewUser() {
        assertThrows(NotFoundException.class, () -> service.update(getNew()));
    }

    @Test
    void delete() {
        assertTrue(service.delete(ADMIN_ID));
    }

    @Test
    void deleteNewUser() {
        assertFalse(service.delete(NOT_FOUND));
    }


}