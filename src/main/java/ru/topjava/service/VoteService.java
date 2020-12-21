package ru.topjava.service;

import com.sun.xml.txw2.DatatypeWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.repository.VoteRepository;
import ru.topjava.utils.LateToUpdate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Vote save (Vote vote){
        DateTimeFormatter todayLimiter = DateTimeFormatter.ofPattern("yyyy-MM-dd 11:00:00");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int voiceId = voteToday(vote.getUser());
        if (voiceId > 0){
            if (vote.getDate().isBefore(LocalDateTime.parse(LocalDateTime.now().format(todayLimiter),formatter))){
                return repository.update(vote, voiceId);
            } else {
                throw new LateToUpdate("too late for update");
            }
        } else {
            return repository.create(vote);
        }
    }

    public int voteToday (User user){
    return repository.hasVoteToday(user);
    }
}
