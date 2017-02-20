package com.steiner.myservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.steiner.myservice.domain.ReviewVector;

import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.web.rest.util.HeaderUtil;
import com.steiner.myservice.service.dto.ReviewVectorDTO;
import com.steiner.myservice.service.mapper.ReviewVectorMapper;
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
 * REST controller for managing ReviewVector.
 */
@RestController
@RequestMapping("/api")
public class ReviewVectorResource {

    private final Logger log = LoggerFactory.getLogger(ReviewVectorResource.class);

    private static final String ENTITY_NAME = "reviewVector";
        
    private final ReviewVectorRepository reviewVectorRepository;

    private final ReviewVectorMapper reviewVectorMapper;

    public ReviewVectorResource(ReviewVectorRepository reviewVectorRepository, ReviewVectorMapper reviewVectorMapper) {
        this.reviewVectorRepository = reviewVectorRepository;
        this.reviewVectorMapper = reviewVectorMapper;
    }

    /**
     * POST  /review-vectors : Create a new reviewVector.
     *
     * @param reviewVectorDTO the reviewVectorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reviewVectorDTO, or with status 400 (Bad Request) if the reviewVector has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/review-vectors")
    @Timed
    public ResponseEntity<ReviewVectorDTO> createReviewVector(@Valid @RequestBody ReviewVectorDTO reviewVectorDTO) throws URISyntaxException {
        log.debug("REST request to save ReviewVector : {}", reviewVectorDTO);
        if (reviewVectorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new reviewVector cannot already have an ID")).body(null);
        }
        ReviewVector reviewVector = reviewVectorMapper.reviewVectorDTOToReviewVector(reviewVectorDTO);
        reviewVector = reviewVectorRepository.save(reviewVector);
        ReviewVectorDTO result = reviewVectorMapper.reviewVectorToReviewVectorDTO(reviewVector);
        return ResponseEntity.created(new URI("/api/review-vectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /review-vectors : Updates an existing reviewVector.
     *
     * @param reviewVectorDTO the reviewVectorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reviewVectorDTO,
     * or with status 400 (Bad Request) if the reviewVectorDTO is not valid,
     * or with status 500 (Internal Server Error) if the reviewVectorDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/review-vectors")
    @Timed
    public ResponseEntity<ReviewVectorDTO> updateReviewVector(@Valid @RequestBody ReviewVectorDTO reviewVectorDTO) throws URISyntaxException {
        log.debug("REST request to update ReviewVector : {}", reviewVectorDTO);
        if (reviewVectorDTO.getId() == null) {
            return createReviewVector(reviewVectorDTO);
        }
        ReviewVector reviewVector = reviewVectorMapper.reviewVectorDTOToReviewVector(reviewVectorDTO);
        reviewVector = reviewVectorRepository.save(reviewVector);
        ReviewVectorDTO result = reviewVectorMapper.reviewVectorToReviewVectorDTO(reviewVector);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reviewVectorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /review-vectors : get all the reviewVectors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reviewVectors in body
     */
    @GetMapping("/review-vectors")
    @Timed
    public List<ReviewVectorDTO> getAllReviewVectors() {
        log.debug("REST request to get all ReviewVectors");
        List<ReviewVector> reviewVectors = reviewVectorRepository.findAll();
        return reviewVectorMapper.reviewVectorsToReviewVectorDTOs(reviewVectors);
    }

    /**
     * GET  /review-vectors/:id : get the "id" reviewVector.
     *
     * @param id the id of the reviewVectorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reviewVectorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/review-vectors/{id}")
    @Timed
    public ResponseEntity<ReviewVectorDTO> getReviewVector(@PathVariable Long id) {
        log.debug("REST request to get ReviewVector : {}", id);
        ReviewVector reviewVector = reviewVectorRepository.findOne(id);
        ReviewVectorDTO reviewVectorDTO = reviewVectorMapper.reviewVectorToReviewVectorDTO(reviewVector);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reviewVectorDTO));
    }

    /**
     * DELETE  /review-vectors/:id : delete the "id" reviewVector.
     *
     * @param id the id of the reviewVectorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/review-vectors/{id}")
    @Timed
    public ResponseEntity<Void> deleteReviewVector(@PathVariable Long id) {
        log.debug("REST request to delete ReviewVector : {}", id);
        reviewVectorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
