package com.steiner.myservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.steiner.myservice.domain.WordOccurrences;

import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.web.rest.util.HeaderUtil;
import com.steiner.myservice.service.dto.WordOccurrencesDTO;
import com.steiner.myservice.service.mapper.WordOccurrencesMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing WordOccurrences.
 */
@RestController
@RequestMapping("/api")
public class WordOccurrencesResource {

    private final Logger log = LoggerFactory.getLogger(WordOccurrencesResource.class);

    private static final String ENTITY_NAME = "wordOccurrences";

    private final WordOccurrencesRepository wordOccurrencesRepository;

    private final WordOccurrencesMapper wordOccurrencesMapper;

    public WordOccurrencesResource(WordOccurrencesRepository wordOccurrencesRepository, WordOccurrencesMapper wordOccurrencesMapper) {
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.wordOccurrencesMapper = wordOccurrencesMapper;
    }

    /**
     * POST /word-occurrences : Create a new wordOccurrences.
     *
     * @param wordOccurrencesDTO the wordOccurrencesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new wordOccurrencesDTO, or with status 400 (Bad Request) if the
     * wordOccurrences has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/word-occurrences")
    @Timed
    public ResponseEntity<WordOccurrencesDTO> createWordOccurrences(@Valid @RequestBody WordOccurrencesDTO wordOccurrencesDTO) throws URISyntaxException {
        log.debug("REST request to save WordOccurrences : {}", wordOccurrencesDTO);
        if (wordOccurrencesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wordOccurrences cannot already have an ID")).body(null);
        }
        WordOccurrences wordOccurrences = wordOccurrencesMapper.wordOccurrencesDTOToWordOccurrences(wordOccurrencesDTO);
        wordOccurrences = wordOccurrencesRepository.save(wordOccurrences);
        WordOccurrencesDTO result = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(wordOccurrences);
        return ResponseEntity.created(new URI("/api/word-occurrences/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /word-occurrences : Updates an existing wordOccurrences.
     *
     * @param wordOccurrencesDTO the wordOccurrencesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * wordOccurrencesDTO, or with status 400 (Bad Request) if the
     * wordOccurrencesDTO is not valid, or with status 500 (Internal Server
     * Error) if the wordOccurrencesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/word-occurrences")
    @Timed
    public ResponseEntity<WordOccurrencesDTO> updateWordOccurrences(@Valid @RequestBody WordOccurrencesDTO wordOccurrencesDTO) throws URISyntaxException {
        log.debug("REST request to update WordOccurrences : {}", wordOccurrencesDTO);
        if (wordOccurrencesDTO.getId() == null) {
            return createWordOccurrences(wordOccurrencesDTO);
        }
        WordOccurrences wordOccurrences = wordOccurrencesMapper.wordOccurrencesDTOToWordOccurrences(wordOccurrencesDTO);
        wordOccurrences = wordOccurrencesRepository.save(wordOccurrences);
        WordOccurrencesDTO result = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(wordOccurrences);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wordOccurrencesDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET /word-occurrences : get all the wordOccurrences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * wordOccurrences in body
     */
    @GetMapping("/word-occurrences")
    @Timed
    public List<WordOccurrencesDTO> getAllWordOccurrences() {
        log.debug("REST request to get all WordOccurrences");
        List<WordOccurrences> wordOccurrences = wordOccurrencesRepository.findAll();
        return wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTOs(wordOccurrences);
    }

    /**
     * GET /word-occurrences : get 500 most frequent words from wordOccurrences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * wordOccurrences in body
     */
    @GetMapping("/word-occurrences/top")
    @Timed
    public List<WordOccurrencesDTO> getTopWordOccurrences() {

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
        return wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTOs(mylist);
    }

    /**
     * GET /word-occurrences/:id : get the "id" wordOccurrences.
     *
     * @param id the id of the wordOccurrencesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * wordOccurrencesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/word-occurrences/{id}")
    @Timed
    public ResponseEntity<WordOccurrencesDTO> getWordOccurrences(@PathVariable Long id) {
        log.debug("REST request to get WordOccurrences : {}", id);
        WordOccurrences wordOccurrences = wordOccurrencesRepository.findOne(id);
        WordOccurrencesDTO wordOccurrencesDTO = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(wordOccurrences);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wordOccurrencesDTO));
    }

    /**
     * DELETE /word-occurrences/:id : delete the "id" wordOccurrences.
     *
     * @param id the id of the wordOccurrencesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/word-occurrences/{id}")
    @Timed
    public ResponseEntity<Void> deleteWordOccurrences(@PathVariable Long id) {
        log.debug("REST request to delete WordOccurrences : {}", id);
        wordOccurrencesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
