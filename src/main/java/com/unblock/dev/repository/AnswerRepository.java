package com.unblock.dev.repository;

import com.unblock.dev.model.Answer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ANSWERS SET votes = votes + :vote WHERE id = :id", nativeQuery = true)
    int incrementVotes(@Param("id") Long id, @Param("vote") int vote);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ANSWERS SET views = views + :view WHERE id = :id", nativeQuery = true)
    int incrementViews(@Param("id") Long id, @Param("view") int view);
}
