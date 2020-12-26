package ru.topjava.repository;

import org.springframework.stereotype.Repository;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
        return repository.save(new Vote(vote));
    }

    public int hasVoteToday(User user) {
        /*Return vote id*/
        Map<String, LocalDateTime> currentDate = getCurrentDate();
        Vote vote;
        try {
            vote = em.createQuery("SELECT v FROM Vote v WHERE v.user =:user AND v.date BETWEEN :start_day AND :end_day", Vote.class)
                    .setParameter("user", user)
                    .setParameter("start_day", currentDate.get("startDay"))
                    .setParameter("end_day", currentDate.get("endDay"))
                    .getSingleResult();
            return vote.getId();
        } catch (javax.persistence.NoResultException e) {
            return -1;
        }
    }

    public Vote update(Vote vote, int voteId) {
        vote.setId(voteId);
        return create(vote);
    }

    public Vote get(@NotNull int id) {
        return repository.findById(id).orElse(null);
    }

    private Map<String, LocalDateTime> getCurrentDate() {
        Map<String, LocalDateTime> result = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        result.put("startDay", now.withHour(0).withMinute(0).withSecond(0));
        result.put("endDay", now.withHour(23).withMinute(59).withMinute(59));
        return result;
    }

}
