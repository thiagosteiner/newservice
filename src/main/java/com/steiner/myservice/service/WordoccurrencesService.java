package com.steiner.myservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
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

    
    public void updateWordOccurrences(Review newReview, Map<String, Integer> myMap) throws JsonProcessingException {

        Set<String> keys = myMap.keySet();
        ArrayList<WordOccurrences> wordOccurrencesList = new ArrayList<>();
        ArrayList<WordOccurrencesDTO> wordOccurrencesDTOList = new ArrayList<>();
        
        ObjectMapper mapper = new ObjectMapper();
        Hibernate5Module module = new Hibernate5Module();
        module.enable(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        module.enable(Feature.FORCE_LAZY_LOADING);
        mapper.registerModule(module);
    
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
        try {
            newReviewVector.setVector(mapper.writeValueAsString(wordOccurrencesDTOList));
        } catch (JsonProcessingException jsonProcessingException) {
        }
        reviewVectorRepository.save(newReviewVector);

    }

}

