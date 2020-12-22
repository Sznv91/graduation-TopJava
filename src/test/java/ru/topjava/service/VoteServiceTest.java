package ru.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.Restaurant;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.to.VoteTo;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(GraduationJpaConfig.class)
@Sql(scripts = "classpath:populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class VoteServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void save() {
        Vote expect = VoteTestData.newVote;
        Vote actual = service.save(expect);
        expect.setId(VoteTestData.newVoteId);
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));
    }

    @Test
    void isVoteToday() {
        assertEquals(VoteTestData.EXIST_VOTE_ID, service.voteToday(UserTestData.admin));
        assertEquals(VoteTestData.NOT_EXIST_VOTE_ID, service.voteToday(UserTestData.user));
    }

    @Test
    void update() {
        Vote expect =  VoteTestData.updatedVoteWithoutId;
        expect.setId(VoteTestData.EXIST_VOTE_ID);
        LocalDateTime limiter = LocalDateTime.now().plusHours(1);
        Vote actual = service.saveWithCustomDateLimiter(expect, limiter);
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));

    }

    @Test
    void updateWithoutVoteId() {
        Vote expect = VoteTestData.updatedVoteWithoutId;
        LocalDateTime limiter = LocalDateTime.now().plusHours(1);
        Vote actual = service.saveWithCustomDateLimiter(expect, limiter);
        expect.setId(VoteTestData.EXIST_VOTE_ID);
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));
    }

}