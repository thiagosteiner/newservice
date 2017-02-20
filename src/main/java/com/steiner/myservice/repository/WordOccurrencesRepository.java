package com.steiner.myservice.repository;

import com.steiner.myservice.domain.WordOccurrences;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WordOccurrences entity.
 */
@SuppressWarnings("unused")
public interface WordOccurrencesRepository extends JpaRepository<WordOccurrences,Long> {
    
 @Query(value = "SELECT word , SUM(amountoccurrences) as occurrences FROM word_occurrences GROUP by word ORDER by occurrences DESC LIMIT 500", nativeQuery = true)

 ArrayList<Object[]> findWords();
 
//
//  SELECT word_id , SUM(amountoccurrences) as occurrences
//  FROM "public".word_occurrences
//  GROUP by word_id
//  ORDER by occurrences DESC
//  LIMIT 500;
    
    
    

}
