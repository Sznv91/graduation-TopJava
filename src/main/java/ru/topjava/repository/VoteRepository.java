package ru.topjava.repository;

import org.springframework.stereotype.Repository;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VoteRepository {

    @PersistenceContext
    EntityManager em;

    VoteCrudRepository repository;

    public VoteRepository(VoteCrudRepository repository) {
        this.repository = repository;
    }

    public Vote create(Vote vote) {
        return repository.save(vote);
    }

    public int hasVoteToday(User user) {
        /*Return vote id*/
        Map<String, LocalDateTime> currentDate = getCurrentDate();

        List result = em.createNativeQuery("SELECT ID FROM VOTES WHERE USER_ID=:id AND DATE_TIME BETWEEN :start_day AND :end_day")
                .setParameter("start_day", currentDate.get("startDay"))
                .setParameter("end_day", currentDate.get("endDay"))
                .setParameter("id", user.getId())
                .getResultList();
        return result.size() > 0 ? (int) result.get(0) : 0;
    }

    public Vote update(Vote vote, int voteId) {
        vote.setId(voteId);
        return create(vote);
    }

    private Map<String, LocalDateTime> getCurrentDate() {
        Map<String, LocalDateTime> result = new HashMap<>();
        DateTimeFormatter startDayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
        DateTimeFormatter endDayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        result.put("startDay", LocalDateTime.parse(LocalDateTime.now().format(startDayFormatter), formatter));
        result.put("endDay", LocalDateTime.parse(LocalDateTime.now().format(endDayFormatter), formatter));
        return result;
    }

}
