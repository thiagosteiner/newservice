package com.steiner.myservice.service;

import com.steiner.myservice.domain.Review;
import com.steiner.myservice.domain.ReviewVector;
import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.service.dto.WordOccurrencesDTO;
import com.steiner.myservice.service.mapper.WordOccurrencesMapper;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordoccurrencesService {

    private final Logger log = LoggerFactory.getLogger(WordoccurrencesService.class);

    private final WordOccurrencesRepository wordOccurrencesRepository;
    private final ReviewVectorRepository reviewVectorRepository;

    public WordoccurrencesService(WordOccurrencesRepository wordOccurrencesRepository,
            ReviewVectorRepository reviewVectorRepository) {

        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.reviewVectorRepository=reviewVectorRepository;
    }

    
    public void updateWordOccurrences(Review newReview, Map<String, Integer> myMap) {

        Set<String> keys = myMap.keySet();
        ArrayList<WordOccurrences> wordOccurrencesList = new ArrayList<WordOccurrences>();
        ArrayList<WordOccurrencesDTO> wordOccurrencesDTOList = new ArrayList<WordOccurrencesDTO>();

        keys.stream().map((key) -> {
            WordOccurrences wordOccurences = new WordOccurrences();
            wordOccurences.setReview(newReview);
            wordOccurences.setAmountoccurrences(myMap.get(key));
            wordOccurences.setWord(key);
            return wordOccurences;
        }).forEachOrdered((wordOccurences) -> {
            wordOccurrencesRepository.save(wordOccurences);
            wordOccurrencesList.add(wordOccurences);
        });

        wordOccurrencesDTOList
                = (ArrayList<WordOccurrencesDTO>) WordOccurrencesMapper.INSTANCE
                        .wordOccurrencesToWordOccurrencesDTOs(wordOccurrencesList);

        ReviewVector newReviewVector = new ReviewVector();
        newReviewVector.setVector(wordOccurrencesDTOList.toString());
        reviewVectorRepository.save(newReviewVector);

    }

}

