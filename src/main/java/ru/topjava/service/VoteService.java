package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.repository.VoteRepository;
import ru.topjava.utils.LateToUpdate;

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
    public Vote save(Vote vote) {
        LocalDateTime todayLimiter = LocalDateTime.now().withHour(11).withMinute(0).withSecond(0);
//        DateTimeFormatter todayLimiter = DateTimeFormatter.ofPattern("yyyy-MM-dd 11:00:00");
        return saveWithCustomDateLimiter(vote, todayLimiter);
    }

    public Vote saveWithCustomDateLimiter(Vote vote, LocalDateTime todayLimiter) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int voiceId = voteToday(vote.getUser());
        if (voiceId >= 0) {
            if (vote.getDate().isBefore(todayLimiter /*LocalDateTime.parse(LocalDateTime.now().format(todayLimiter*/)) {
                return repository.update(vote, voiceId);
            } else {
                throw new LateToUpdate("too late for update");
            }
        } else {
            return repository.create(vote);
        }
    }

    public Vote get(int id) {
        return repository.get(id);
    }

    public int voteToday(User user) {
        return repository.hasVoteToday(user);
    }
}
