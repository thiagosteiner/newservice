package com.steiner.myservice.service;

import com.steiner.myservice.domain.Book;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.domain.ReviewVector;
import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.repository.BookRepository;
import com.steiner.myservice.repository.ReviewRepository;
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
public class CreatereviewcsvService {

    private final Logger log = LoggerFactory.getLogger(CreatereviewcsvService.class);

    private final ReviewRepository reviewRepository;
    private final ReviewVectorRepository reviewVectorRepository;
 
    private final WordOccurrencesRepository wordOccurrencesRepository;

    public CreatereviewcsvService(ReviewRepository reviewRepository,
            WordOccurrencesRepository wordOccurrencesRepository,
            BookRepository bookRepository,ReviewVectorRepository reviewVectorRepository) {
        this.reviewRepository = reviewRepository;
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.reviewVectorRepository=reviewVectorRepository;
           }

    @Async
    public void createreviewcsv(String mystring, Book book) {
        UtilService utilService = new UtilService();
        Review newReview = new Review();
        newReview.setBook(book);
         newReview.setReviewstring("string");
        newReview.setReviewtext(mystring);
        Map<String, Integer> myMap;
        myMap = utilService.CountWords(mystring);
        newReview = reviewRepository.save(newReview);
        WordoccurrencesService wordoccurrencesService
                = new WordoccurrencesService(wordOccurrencesRepository,reviewVectorRepository);
         wordoccurrencesService.updateWordOccurrences(newReview, myMap);

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

