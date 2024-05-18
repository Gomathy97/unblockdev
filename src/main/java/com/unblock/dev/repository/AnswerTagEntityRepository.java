package com.unblock.dev.repository;

import com.unblock.dev.model.AnswerTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerTagEntityRepository extends JpaRepository<AnswerTagEntity, Long> {
}
