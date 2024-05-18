package com.unblock.dev.repository;

import com.unblock.dev.model.AnswerEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerEntityRepository extends JpaRepository<AnswerEntity, Long> {
    @Query ("SELECT A from AnswerEntity A " +
            "where A.answer.id = :ansId " +
            "order by A.id " +
            "limit 1")
    Optional<AnswerEntity> findByAnswerRefId(@NonNull Long ansId);
}
