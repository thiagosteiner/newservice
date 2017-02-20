package com.steiner.myservice.service;

import com.steiner.myservice.domain.Book;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.domain.Word;
import com.steiner.myservice.repository.BookRepository;
import com.steiner.myservice.repository.ReviewRepository;
import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.repository.WordRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CsvService {

    private final Logger log = LoggerFactory.getLogger(CsvService.class);

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final WordOccurrencesRepository wordOccurrencesRepository;
    private final ReviewVectorRepository reviewVectorRepository;

    public CsvService(ReviewRepository reviewRepository, BookRepository bookRepository,
            WordOccurrencesRepository wordOccurrencesRepository,ReviewVectorRepository reviewVectorRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.reviewVectorRepository=reviewVectorRepository;
    }

    @Async
    public void processReviewFromCsvFile(String csvfilepath, Long bookId) throws FileNotFoundException, IOException {

        String mystring;
        UtilService utilService = new UtilService();
        Pattern myPatterCompileColumnCatcher
                = Pattern.compile(".*?\\t.*?\\t.*?\\t(\\\".*\\\")");
        int line = 0;
        // Start time
        long startTime = System.nanoTime();
        Book book;
        book = bookRepository.findOne(bookId);
        Optional<Book> existingBook;
        existingBook = Optional.ofNullable(bookRepository.findOne(bookId));

        if (existingBook.isPresent()) {

            try (BufferedReader myBufferedReader = new BufferedReader(new FileReader(csvfilepath))) {

                while ((mystring = myBufferedReader.readLine()) != null) {
                    //System.out.println(mystring);
                    line += 1;
                    System.out.println("Line:" + line);
                    Matcher myMacher = myPatterCompileColumnCatcher.matcher(mystring);

                    if (myMacher.find()) {
                        mystring = Jsoup.parse(myMacher.group(1).toLowerCase()).text();//Remove html
                        mystring = mystring.trim();
                        if (mystring != null && !mystring.isEmpty()) {
                            mystring = utilService.cleanString(mystring);
                            CreatereviewcsvService createreviewcsvService
                                    = new CreatereviewcsvService(reviewRepository,
                                            wordOccurrencesRepository,
                                            bookRepository,reviewVectorRepository);
                            createreviewcsvService.createreviewcsv(mystring, book);

                        }
                    }

                }

                myBufferedReader.close();
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                System.out.println("Tempo(milisegundos)=" + TimeUnit.NANOSECONDS.toMillis(duration));

            } catch (IOException e) {
                log.warn("csv file stop in line '{}'", line, e);
            }
        } else {
            log.debug("Book not bookId:{}", bookId);
        }

    }

}
