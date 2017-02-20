package com.steiner.myservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.steiner.myservice.domain.Review;

import com.steiner.myservice.repository.ReviewRepository;
import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.service.CsvService;
import com.steiner.myservice.service.ReviewService;
import com.steiner.myservice.web.rest.util.HeaderUtil;
import com.steiner.myservice.service.dto.ReviewDTO;
import com.steiner.myservice.service.mapper.ReviewMapper;
import io.github.jhipster.web.util.ResponseUtil;
import java.io.IOException;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * REST controller for managing Review.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReviewResource {

    private final Logger log = LoggerFactory.getLogger(ReviewResource.class);

    private static final String ENTITY_NAME = "review";

    private final ReviewRepository reviewRepository;
    
    private final ReviewVectorRepository reviewVectorRepository;
    
    private final CsvService csvService;

    private final ReviewService reviewService;

    private final ReviewMapper reviewMapper;

    public ReviewResource(ReviewRepository reviewRepository, ReviewMapper reviewMapper,
            ReviewService reviewService,
            CsvService csvService,ReviewVectorRepository reviewVectorRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.reviewService = reviewService;
        this.csvService = csvService;
        this.reviewVectorRepository=reviewVectorRepository;

    }


    /**
     * POST /reviews : Create a new review.
     *
     * @param reviewDTO the reviewDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new reviewDTO, or with status 400 (Bad Request) if the review has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reviews")
    @Timed
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO) throws URISyntaxException {
        log.debug("REST request to save Review : {}", reviewDTO);
        if (reviewDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new review cannot already have an ID")).body(null);
        }
        //Review review = reviewMapper.reviewDTOToReview(reviewDTO);
        //review = reviewRepository.save(review);
        Review review = reviewService.createReview(reviewDTO);
        ReviewDTO result = reviewMapper.reviewToReviewDTO(review);
        return ResponseEntity.created(new URI("/api/reviews/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * POST /reviews/csv: Create reviews from CSV file.
     *
     * @param reviewDTO the reviewDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new reviewDTO, or with status 400 (Bad Request) if the review has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reviews/csv")
    @Timed
    public ResponseEntity<ReviewDTO> createReviewFromCSVFile(@Valid @RequestBody ReviewDTO reviewDTO) throws URISyntaxException {
        log.debug("REST request to save Review : {}", reviewDTO);
        if (reviewDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new review cannot already have an ID")).body(null);
        }

        Long bookId = reviewDTO.getBookId();
        String path = reviewDTO.getReviewtext();

        try {
            csvService.processReviewFromCsvFile(path, bookId);
        } catch (IOException ex) {
            log.warn("Error in createReviewFromCSVFile", ex);
        }
        return ResponseEntity.ok()
                .body(null);
    }
    /**
     * PUT  /reviews : Updates an existing review.
     *
     * @param reviewDTO the reviewDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reviewDTO,
     * or with status 400 (Bad Request) if the reviewDTO is not valid,
     * or with status 500 (Internal Server Error) if the reviewDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reviews")
    @Timed
    public ResponseEntity<ReviewDTO> updateReview(@Valid @RequestBody ReviewDTO reviewDTO) throws URISyntaxException {
        log.debug("REST request to update Review : {}", reviewDTO);
        if (reviewDTO.getId() == null) {
            return createReview(reviewDTO);
        }
        Review review = reviewMapper.reviewDTOToReview(reviewDTO);
        review = reviewRepository.save(review);
        ReviewDTO result = reviewMapper.reviewToReviewDTO(review);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reviewDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reviews : get all the reviews.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reviews in body
     */
    @GetMapping("/reviews")
    @Timed
    public List<ReviewDTO> getAllReviews() {
        log.debug("REST request to get all Reviews");
        List<Review> reviews = reviewRepository.findAll();
        return reviewMapper.reviewsToReviewDTOs(reviews);
    }

    /**
     * GET  /reviews/:id : get the "id" review.
     *
     * @param id the id of the reviewDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reviewDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reviews/{id}")
    @Timed
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id) {
        log.debug("REST request to get Review : {}", id);
        Review review = reviewRepository.findOne(id);
        ReviewDTO reviewDTO = reviewMapper.reviewToReviewDTO(review);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reviewDTO));
    }

    /**
     * DELETE  /reviews/:id : delete the "id" review.
     *
     * @param id the id of the reviewDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reviews/{id}")
    @Timed
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        log.debug("REST request to delete Review : {}", id);
        reviewRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
