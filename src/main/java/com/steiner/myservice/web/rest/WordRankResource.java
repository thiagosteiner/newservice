package com.steiner.myservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.steiner.myservice.domain.WordRank;

import com.steiner.myservice.repository.WordRankRepository;
import com.steiner.myservice.web.rest.util.HeaderUtil;
import com.steiner.myservice.service.dto.WordRankDTO;
import com.steiner.myservice.service.mapper.WordRankMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing WordRank.
 */
@RestController
@RequestMapping("/api")
public class WordRankResource {

    private final Logger log = LoggerFactory.getLogger(WordRankResource.class);

    private static final String ENTITY_NAME = "wordRank";
        
    private final WordRankRepository wordRankRepository;

    private final WordRankMapper wordRankMapper;

    public WordRankResource(WordRankRepository wordRankRepository, WordRankMapper wordRankMapper) {
        this.wordRankRepository = wordRankRepository;
        this.wordRankMapper = wordRankMapper;
    }

    /**
     * POST  /word-ranks : Create a new wordRank.
     *
     * @param wordRankDTO the wordRankDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wordRankDTO, or with status 400 (Bad Request) if the wordRank has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/word-ranks")
    @Timed
    public ResponseEntity<WordRankDTO> createWordRank(@Valid @RequestBody WordRankDTO wordRankDTO) throws URISyntaxException {
        log.debug("REST request to save WordRank : {}", wordRankDTO);
        if (wordRankDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wordRank cannot already have an ID")).body(null);
        }
        WordRank wordRank = wordRankMapper.wordRankDTOToWordRank(wordRankDTO);
        wordRank = wordRankRepository.save(wordRank);
        WordRankDTO result = wordRankMapper.wordRankToWordRankDTO(wordRank);
        return ResponseEntity.created(new URI("/api/word-ranks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /word-ranks : Updates an existing wordRank.
     *
     * @param wordRankDTO the wordRankDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wordRankDTO,
     * or with status 400 (Bad Request) if the wordRankDTO is not valid,
     * or with status 500 (Internal Server Error) if the wordRankDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/word-ranks")
    @Timed
    public ResponseEntity<WordRankDTO> updateWordRank(@Valid @RequestBody WordRankDTO wordRankDTO) throws URISyntaxException {
        log.debug("REST request to update WordRank : {}", wordRankDTO);
        if (wordRankDTO.getId() == null) {
            return createWordRank(wordRankDTO);
        }
        WordRank wordRank = wordRankMapper.wordRankDTOToWordRank(wordRankDTO);
        wordRank = wordRankRepository.save(wordRank);
        WordRankDTO result = wordRankMapper.wordRankToWordRankDTO(wordRank);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wordRankDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /word-ranks : get all the wordRanks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wordRanks in body
     */
    @GetMapping("/word-ranks")
    @Timed
    public List<WordRankDTO> getAllWordRanks() {
        log.debug("REST request to get all WordRanks");
        List<WordRank> wordRanks = wordRankRepository.findAll();
        return wordRankMapper.wordRanksToWordRankDTOs(wordRanks);
    }

    /**
     * GET  /word-ranks/:id : get the "id" wordRank.
     *
     * @param id the id of the wordRankDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wordRankDTO, or with status 404 (Not Found)
     */
    @GetMapping("/word-ranks/{id}")
    @Timed
    public ResponseEntity<WordRankDTO> getWordRank(@PathVariable Long id) {
        log.debug("REST request to get WordRank : {}", id);
        WordRank wordRank = wordRankRepository.findOne(id);
        WordRankDTO wordRankDTO = wordRankMapper.wordRankToWordRankDTO(wordRank);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wordRankDTO));
    }

    /**
     * DELETE  /word-ranks/:id : delete the "id" wordRank.
     *
     * @param id the id of the wordRankDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/word-ranks/{id}")
    @Timed
    public ResponseEntity<Void> deleteWordRank(@PathVariable Long id) {
        log.debug("REST request to delete WordRank : {}", id);
        wordRankRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
