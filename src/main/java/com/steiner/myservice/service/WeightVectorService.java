package com.steiner.myservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.steiner.myservice.domain.ReviewVector;
import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.service.dto.ReviewVectorDTO;
import com.steiner.myservice.service.dto.WordOccurrencesDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WeightVectorService {

    private final Logger log = LoggerFactory.getLogger(WeightVectorService.class);
    private final WordOccurrencesRepository wordOccurrencesRepository;
    private final ReviewVectorRepository reviewVectorRepository;

    public WeightVectorService(WordOccurrencesRepository wordOccurrencesRepository, ReviewVectorRepository reviewVectorRepository) {
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.reviewVectorRepository = reviewVectorRepository;
    }

    public ReviewVectorDTO getWeightVector(Long id) throws IOException {
        log.info("Service request to get WeightVector : {}", id);
        ObjectMapper mapper = new ObjectMapper();
        Hibernate5Module module = new Hibernate5Module();
        module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        mapper.registerModule(module);

        ReviewVector reviewVector = reviewVectorRepository.findOne(id);
        String vectorJson = reviewVector.getVector();
        ArrayList<WordOccurrencesDTO> wordOccurrencesDTOList
                = mapper.readValue(vectorJson,
                        mapper.getTypeFactory()
                                .constructCollectionType(ArrayList.class, WordOccurrencesDTO.class));

        TopWordOccurrencesService topWordOccurrencesService
                = new TopWordOccurrencesService(wordOccurrencesRepository);
        ArrayList<WordOccurrences> topWordOccurrenceslist = (ArrayList<WordOccurrences>) topWordOccurrencesService.getTopWords();

        topWordOccurrenceslist.sort((e1, e2) -> Integer.compare(e1.getAmountoccurrences(),
                e2.getAmountoccurrences()));
        Collections.reverse(topWordOccurrenceslist);

        ArrayList<String> topWordslist = new ArrayList<>();

        topWordOccurrenceslist.stream().map(wordOccurrences -> {
            String x = wordOccurrences.getWord();
            return x;
        }).forEachOrdered((x) -> {
            topWordslist.add(x);
        });

        Integer quantityinunion = wordOccurrencesDTOList.stream()
                .filter(x -> topWordslist.contains(x.getWord())).collect(Collectors.summingInt(x -> x.getAmountoccurrences()));

        LinkedHashMap<String, Double> wordOccurrencesDTOMap = new LinkedHashMap<>();

        LinkedHashMap<String, Double> weightwordOccurrences = new LinkedHashMap<>();

        wordOccurrencesDTOList.stream().forEachOrdered((x) -> {

            wordOccurrencesDTOMap.put(x.getWord(), (double) x.getAmountoccurrences());

        });

        topWordslist.stream().forEachOrdered((word) -> {
            if (wordOccurrencesDTOMap.containsKey(word)) {
                weightwordOccurrences.put(word, wordOccurrencesDTOMap.get(word) / quantityinunion);

            } else {
                weightwordOccurrences.put(word, 0.0);

            }
        });

        String weightvectorJson = mapper.writeValueAsString(weightwordOccurrences);
        ReviewVectorDTO weightvector = new ReviewVectorDTO();
        weightvector.setVector(weightvectorJson);
        weightvector.setId(id);
        log.info("Finish process get WeightVector : {}", weightvectorJson);
        return weightvector;

    }

}
