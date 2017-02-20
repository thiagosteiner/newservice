package com.steiner.myservice.repository;

import com.steiner.myservice.domain.WordRank;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WordRank entity.
 */
@SuppressWarnings("unused")
public interface WordRankRepository extends JpaRepository<WordRank,Long> {

}
