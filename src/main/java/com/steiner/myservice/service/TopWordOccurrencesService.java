package com.steiner.myservice.service;

import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TopWordOccurrencesService {

    private final Logger log = LoggerFactory.getLogger(TopWordOccurrencesService.class);

    private final WordOccurrencesRepository wordOccurrencesRepository;

    public TopWordOccurrencesService(WordOccurrencesRepository wordOccurrencesRepository) {

        this.wordOccurrencesRepository = wordOccurrencesRepository;

    }

    public List<WordOccurrences> getTopWords() {

        ArrayList<Object[]> findTopWords = wordOccurrencesRepository.findTopWords();
        List<WordOccurrences> mylist = new ArrayList<>();
        int i = 0;
        while (i < findTopWords.size()) {
            WordOccurrences myWordOccurrence = new WordOccurrences();
            myWordOccurrence.setWord((String) findTopWords.get(i)[0]);
            myWordOccurrence.setAmountoccurrences(Integer.parseInt(findTopWords.get(i)[1].toString()));
            mylist.add(myWordOccurrence);
            i += 1;
        }
        log.info("getTopWords");
        return mylist;

    }

}
