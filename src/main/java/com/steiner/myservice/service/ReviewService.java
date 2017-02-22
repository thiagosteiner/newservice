package com.steiner.myservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.steiner.myservice.domain.Book;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.repository.BookRepository;
import com.steiner.myservice.repository.ReviewRepository;
import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.repository.WordRepository;
import com.steiner.myservice.service.dto.ReviewDTO;
import java.util.Map;
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

    public Review createReview(ReviewDTO reviewDTO) throws JsonProcessingException {
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
    

}

