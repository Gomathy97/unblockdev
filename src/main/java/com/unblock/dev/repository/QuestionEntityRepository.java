package com.unblock.dev.repository;

import com.unblock.dev.model.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionEntityRepository extends JpaRepository<QuestionEntity, Long> {
}
