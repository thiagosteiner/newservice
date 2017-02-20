package com.steiner.myservice.service;

import com.steiner.myservice.domain.Book;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.domain.ReviewVector;
import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.repository.BookRepository;
import com.steiner.myservice.repository.ReviewRepository;
import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.repository.WordRepository;
import com.steiner.myservice.service.dto.ReviewDTO;
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
public class ReviewService {

    private final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final WordOccurrencesRepository wordOccurrencesRepository;
    private final ReviewVectorRepository reviewVectorRepository;

    public ReviewService(ReviewRepository reviewRepository,
            WordRepository wordRepository,
            WordOccurrencesRepository wordOccurrencesRepository,
            BookRepository bookRepository,ReviewVectorRepository reviewVectorRepository) {
        this.reviewRepository = reviewRepository;
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.bookRepository = bookRepository;
        this.reviewVectorRepository=reviewVectorRepository;
    }

    public Review createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        UtilService utilService = new UtilService();
        WordoccurrencesService wordoccurrencesService
                = new WordoccurrencesService(wordOccurrencesRepository,reviewVectorRepository);
        Book book = bookRepository.findOne(reviewDTO.getBookId());
        review.setBook(book);
        String cleanreviewstring = utilService.cleanString(reviewDTO.getReviewstring());
        review.setReviewstring(cleanreviewstring);
        String cleanreviewtext = utilService.cleanString(reviewDTO.getReviewtext());
        //Todo test reviewtext null or empity
        review.setReviewtext(cleanreviewtext);
        Map<String, Integer> myMap;
        myMap = utilService.CountWords(cleanreviewtext);
        review = reviewRepository.save(review);
        wordoccurrencesService.updateWordOccurrences(review, myMap);
        log.debug("Created Review: {}", review);
        return review;
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

