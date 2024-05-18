package com.unblock.dev.repository;

import com.unblock.dev.model.QuestionScore;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionScoreRepository extends JpaRepository<QuestionScore, Long> {
    @Query("SELECT S from QuestionScore S " +
            "where S.question.id = :id")
    Optional<QuestionScore> findByQuestionId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE QuestionScore S SET " +
            "S.activeScore = :active, S.newestScore = :newest where S.ID = :id")
    void updateScore(@Param("active") String active, @Param("newest") String newest, @Param("id") Long id);

    @Query("SELECT S from QuestionScore S " +
            "order by :filter desc")
    List<QuestionScore> findByFilter(@Param("filter") String filter, Pageable pageable);
}
