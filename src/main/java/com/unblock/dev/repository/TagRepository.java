package com.unblock.dev.repository;

import com.unblock.dev.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tags, Long> {
    @Query("SELECT T FROM Tags T " +
            "where (T.tag like %:text% or T.tag like %:text%) ")
    List<Tags> findTagsByText(@Param("text") String text);

    @Query("SELECT T from Tags T " +
            "where T.tag in :tag ")
    List<Tags> findAllByTag(@Param("tag") String[] tag);

}
