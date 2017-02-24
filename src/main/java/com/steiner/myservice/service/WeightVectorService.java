package com.steiner.myservice.service;

import aj.org.objectweb.asm.TypeReference;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        List<WordOccurrences> topWordOccurrenceslist = topWordOccurrencesService.getTopWords();

        ArrayList<String> topWordslist = topWordOccurrenceslist.stream().map(wordOccurrences -> {
            return wordOccurrences.getWord();
        }).collect(Collectors.toCollection(ArrayList::new));

        Integer quantityinunion = wordOccurrencesDTOList.stream()
                .filter(e -> topWordslist.contains(e.getWord())).collect(Collectors.summingInt(e -> e.getAmountoccurrences()));

        Map<String, Double> wordOccurrencesDTOMap;

        Map<String, Double> weightwordOccurrences = new HashMap<>();

        wordOccurrencesDTOMap
                = wordOccurrencesDTOList.stream()
                        .collect(Collectors.toMap(x -> x.getWord(),
                                x -> {
                                    return ((double) x.getAmountoccurrences());

                                }
                        ));

        topWordslist.forEach((word) -> {
            if (wordOccurrencesDTOMap.containsKey(word)) {
                weightwordOccurrences.put(word, wordOccurrencesDTOMap.get(word) / quantityinunion);

            } else {
                weightwordOccurrences.put(word, 0.0);

            }
        });

        String weightvectorJson = mapper.writeValueAsString(weightwordOccurrences);
        ReviewVectorDTO weightvector = new ReviewVectorDTO();
        weightvector.setVector(weightvectorJson);
        log.info("Finish process get WeightVector : {}", weightvectorJson);
        return weightvector;

    }

}
