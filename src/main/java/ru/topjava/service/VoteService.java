package ru.topjava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.entity.User;
import ru.topjava.entity.Vote;
import ru.topjava.repository.VoteRepository;
import ru.topjava.utils.ExistException;
import ru.topjava.utils.LateToUpdate;
import ru.topjava.utils.NotFoundException;

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
        if (voteToday(vote.getUser()) <= 0) {
            return repository.create(vote);
        } else {
            throw new ExistException("User id: " + vote.getUser().getId() + " has already voted today.");
        }
    }

    @Transactional
    public Vote update(Vote vote) {
        LocalDateTime todayLimiter = LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0);
        return updateWithCustomDateLimiter(vote, todayLimiter);
    }

    @Transactional
    public Vote updateWithCustomDateLimiter(Vote vote, LocalDateTime todayLimiter) {
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
            throw new NotFoundException("To re-vote, you must first perform a vote.");
        }
    }

    public Vote get(int id) {
        return repository.get(id);
    }

    public int voteToday(User user) {
        return repository.hasVoteToday(user);
    }

    public Map<Integer, Integer> getRestaurantsCount(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.getVoteMap(startDate, endDate);
    }
}
