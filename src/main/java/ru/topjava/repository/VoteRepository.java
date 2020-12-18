package ru.topjava.repository;

import org.springframework.stereotype.Repository;
import ru.topjava.entity.Vote;

@Repository
public class VoteRepository {

    private final VoteCrudRepository repository;

    public VoteRepository(VoteCrudRepository repository) {
        this.repository = repository;
    }

    public void save(Vote vote){
        repository.save(vote);
    }

    public Vote get (int id){
        return repository.findById(id).orElse(null);
    }
}
