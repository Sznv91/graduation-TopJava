package ru.topjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.topjava.entity.Vote;

public interface VoteCrudRepository extends JpaRepository<Vote, Integer> {
}
