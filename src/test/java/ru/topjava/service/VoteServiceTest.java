package ru.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.topjava.config.GraduationJpaConfig;
import ru.topjava.entity.Vote;
import ru.topjava.to.VoteTo;
import ru.topjava.utils.NotFoundException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(GraduationJpaConfig.class)
@Sql(scripts = {"classpath:initDB_H2.sql", "classpath:populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
class VoteServiceTest {

    private final LocalDateTime limiter = LocalDateTime.now().plusMinutes(2);

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
        Vote expect = VoteTestData.updatedVoteWithoutId;
        expect.setId(VoteTestData.EXIST_VOTE_ID);
        Vote actual = service.updateWithCustomDateLimiter(expect, limiter);
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));

    }

    @Test
    void updateWithoutVoteId() {
        Vote expect = VoteTestData.updatedVoteWithoutId;
        Vote actual = service.updateWithCustomDateLimiter(expect, limiter);
        expect.setId(VoteTestData.EXIST_VOTE_ID);
        assertEquals(VoteTo.getVoteTo(expect), VoteTo.getVoteTo(actual));
    }

    @Test
    void updateNotExist() {
        Vote expect = VoteTestData.newVote;
        assertThrows(NotFoundException.class, () -> service.update(expect));
    }

}