package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.repository.VoteRepository;
import ru.topjava.utils.LateToUpdate;

import java.time.LocalDateTime;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Vote save(Vote vote) {
        LocalDateTime todayLimiter = LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0);
        return saveWithCustomDateLimiter(vote, todayLimiter);
    }

    @Transactional
    public Vote saveWithCustomDateLimiter(Vote vote, LocalDateTime todayLimiter) {
        int voiceId = voteToday(vote.getUser());
        if (voiceId >= 0) {
            if (vote.getDate().isBefore(todayLimiter)) {
                return repository.update(vote, voiceId);
            } else {
                throw new LateToUpdate("too late for update. Vote time: " + vote.getDate() +
                        ". TodayLimiter is: " + todayLimiter +
                        ". Restaurant id: " + vote.getRestaurant().getId());
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

    public Map<Integer, Integer> getRestaurantsCount (LocalDateTime startDate, LocalDateTime endDate){
        return repository.getVoteMap(startDate, endDate);
    }
}
