package com.steiner.myservice.repository;

import com.steiner.myservice.domain.WordOccurrences;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WordOccurrences entity.
 */
@SuppressWarnings("unused")
public interface WordOccurrencesRepository extends JpaRepository<WordOccurrences,Long> {
 
 @Query(value = "SELECT word , SUM(amountoccurrences) as occurrences FROM word_occurrences GROUP by word ORDER by occurrences DESC LIMIT 500", nativeQuery = true)
 ArrayList<Object[]> findTopWords();
 
}
