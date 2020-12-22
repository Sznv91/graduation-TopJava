package ru.topjava.repository;

import org.springframework.stereotype.Repository;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
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
        Vote vote;
        try{
            vote = em.createQuery("SELECT v FROM Vote v WHERE v.user =:user AND v.date BETWEEN :start_day AND :end_day", Vote.class)
                    .setParameter("user", user)
                    .setParameter("start_day", currentDate.get("startDay"))
                    .setParameter("end_day", currentDate.get("endDay"))
                    .getSingleResult();
            return vote.getId();
        } catch (javax.persistence.NoResultException e){
            return -1;
        }

       /* List result = em.createNativeQuery("SELECT ID FROM VOTES WHERE USER_ID=:id AND DATE_TIME BETWEEN :start_day AND :end_day")
                .setParameter("start_day", currentDate.get("startDay"))
                .setParameter("end_day", currentDate.get("endDay"))
                .setParameter("id", user.getId())
                .getResultList();*/
        //result.size() > 0 ? (int) result.get(0) : 0;
    }

    public Vote update(Vote vote, int voteId) {
        vote.setId(voteId);
        return create(vote);
    }

    public Vote get(@NotNull int id){
        return repository.findById(id).orElse(null);
    }

    private Map<String, LocalDateTime> getCurrentDate() {
        Map<String, LocalDateTime> result = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter startDayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
//        DateTimeFormatter endDayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        result.put("startDay", now.withHour(0).withMinute(0).withSecond(0));//LocalDateTime.parse(LocalDateTime.now().format(startDayFormatter), formatter));
        result.put("endDay", now.withHour(23).withMinute(59).withMinute(59));//LocalDateTime.parse(LocalDateTime.now().format(endDayFormatter), formatter));
        return result;
    }

}
