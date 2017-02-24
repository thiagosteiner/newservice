package com.steiner.myservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.steiner.myservice.domain.Book;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.repository.BookRepository;
import com.steiner.myservice.repository.ReviewRepository;
import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateReviewCsvService {

    private final Logger log = LoggerFactory.getLogger(CreateReviewCsvService.class);

    private final ReviewRepository reviewRepository;
    private final ReviewVectorRepository reviewVectorRepository;

    private final WordOccurrencesRepository wordOccurrencesRepository;

    public CreateReviewCsvService(ReviewRepository reviewRepository,
            WordOccurrencesRepository wordOccurrencesRepository,
            BookRepository bookRepository, ReviewVectorRepository reviewVectorRepository) {
        this.reviewRepository = reviewRepository;
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.reviewVectorRepository = reviewVectorRepository;
    }

    @Async
    public void createreviewcsv(String mystring, Book book) throws JsonProcessingException {
        UtilService utilService = new UtilService();
        Review newReview = new Review();
        newReview.setBook(book);
        newReview.setReviewstring("string");
        newReview.setReviewtext(mystring);
        Map<String, Integer> myMap;
        myMap = utilService.CountWords(mystring);
        newReview = reviewRepository.save(newReview);
        WordOccurrencesService wordOccurrencesService
                = new WordOccurrencesService(wordOccurrencesRepository, reviewVectorRepository);
        wordOccurrencesService.updateWordOccurrences(newReview, myMap);

    }

}
