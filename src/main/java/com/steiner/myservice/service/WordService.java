package com.steiner.myservice.service;

import com.steiner.myservice.domain.Word;
import com.steiner.myservice.repository.WordRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordService {

    private final Logger log = LoggerFactory.getLogger(WordService.class);

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;

    }

    public Word findOrSaveWord(String wordstring) {
        Word word = new Word();
        word.setWordstring(wordstring);
//        Optional<Word> existingWord=wordRepository.findByWordstring(wordstring);
//        if (!existingWord.isPresent()) {

        word = wordRepository.save(word);

//        } else{
//            word=existingWord.get();
//        
//        }
        return word;
    }

}
