package com.unblock.dev.repository;

import com.unblock.dev.model.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long>, JpaRepository<Question, Long> {
    @Query("SELECT Q FROM Question Q " +
            "where (Q.title like %:text% or Q.description like %:text%) " +
            "order by Q.timeCreated desc")
    List<Question> findQuestionByText(@Param("text") String text, Pageable pageable);

    Optional<Question> findById(@Param("id") Long id);

    List<Question> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query ("SELECT distinct Q from Question Q, QuestionTagEntity E " +
            "where E.tag.tag in :tags")
    List<Question> findQuestionsByTagsIds(@Param("tags") List<String> tags, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE QUESTIONS SET votes = votes + :vote WHERE id = :id", nativeQuery = true)
    int incrementVotes(@Param("id") Long id, @Param("vote") int vote);

    @Modifying
    @Transactional
    @Query(value = "UPDATE QUESTIONS SET views = views + :view WHERE id = :id", nativeQuery = true)
    int incrementViews(@Param("id") Long id, @Param("view") int view);

    @Modifying
    @Transactional
    @Query(value = "UPDATE QUESTIONS SET resolved = true WHERE id = :id", nativeQuery = true)
    int updateResolved(@Param("id") Long id);

}
