package com.steiner.myservice.web.rest;

import com.steiner.myservice.MyserviceApp;

import com.steiner.myservice.domain.ReviewVector;
import com.steiner.myservice.repository.ReviewVectorRepository;
import com.steiner.myservice.service.dto.ReviewVectorDTO;
import com.steiner.myservice.service.mapper.ReviewVectorMapper;
import com.steiner.myservice.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReviewVectorResource REST controller.
 *
 * @see ReviewVectorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyserviceApp.class)
public class ReviewVectorResourceIntTest {

    private static final String DEFAULT_VECTOR = "AAAAAAAAAA";
    private static final String UPDATED_VECTOR = "BBBBBBBBBB";

    @Autowired
    private ReviewVectorRepository reviewVectorRepository;

    @Autowired
    private ReviewVectorMapper reviewVectorMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReviewVectorMockMvc;

    private ReviewVector reviewVector;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            ReviewVectorResource reviewVectorResource = new ReviewVectorResource(reviewVectorRepository, reviewVectorMapper);
        this.restReviewVectorMockMvc = MockMvcBuilders.standaloneSetup(reviewVectorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReviewVector createEntity(EntityManager em) {
        ReviewVector reviewVector = new ReviewVector()
                .vector(DEFAULT_VECTOR);
        return reviewVector;
    }

    @Before
    public void initTest() {
        reviewVector = createEntity(em);
    }

    @Test
    @Transactional
    public void createReviewVector() throws Exception {
        int databaseSizeBeforeCreate = reviewVectorRepository.findAll().size();

        // Create the ReviewVector
        ReviewVectorDTO reviewVectorDTO = reviewVectorMapper.reviewVectorToReviewVectorDTO(reviewVector);

        restReviewVectorMockMvc.perform(post("/api/review-vectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewVectorDTO)))
            .andExpect(status().isCreated());

        // Validate the ReviewVector in the database
        List<ReviewVector> reviewVectorList = reviewVectorRepository.findAll();
        assertThat(reviewVectorList).hasSize(databaseSizeBeforeCreate + 1);
        ReviewVector testReviewVector = reviewVectorList.get(reviewVectorList.size() - 1);
        assertThat(testReviewVector.getVector()).isEqualTo(DEFAULT_VECTOR);
    }

    @Test
    @Transactional
    public void createReviewVectorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reviewVectorRepository.findAll().size();

        // Create the ReviewVector with an existing ID
        ReviewVector existingReviewVector = new ReviewVector();
        existingReviewVector.setId(1L);
        ReviewVectorDTO existingReviewVectorDTO = reviewVectorMapper.reviewVectorToReviewVectorDTO(existingReviewVector);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewVectorMockMvc.perform(post("/api/review-vectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingReviewVectorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReviewVector> reviewVectorList = reviewVectorRepository.findAll();
        assertThat(reviewVectorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkVectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewVectorRepository.findAll().size();
        // set the field null
        reviewVector.setVector(null);

        // Create the ReviewVector, which fails.
        ReviewVectorDTO reviewVectorDTO = reviewVectorMapper.reviewVectorToReviewVectorDTO(reviewVector);

        restReviewVectorMockMvc.perform(post("/api/review-vectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewVectorDTO)))
            .andExpect(status().isBadRequest());

        List<ReviewVector> reviewVectorList = reviewVectorRepository.findAll();
        assertThat(reviewVectorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReviewVectors() throws Exception {
        // Initialize the database
        reviewVectorRepository.saveAndFlush(reviewVector);

        // Get all the reviewVectorList
        restReviewVectorMockMvc.perform(get("/api/review-vectors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reviewVector.getId().intValue())))
            .andExpect(jsonPath("$.[*].vector").value(hasItem(DEFAULT_VECTOR.toString())));
    }

    @Test
    @Transactional
    public void getReviewVector() throws Exception {
        // Initialize the database
        reviewVectorRepository.saveAndFlush(reviewVector);

        // Get the reviewVector
        restReviewVectorMockMvc.perform(get("/api/review-vectors/{id}", reviewVector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reviewVector.getId().intValue()))
            .andExpect(jsonPath("$.vector").value(DEFAULT_VECTOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReviewVector() throws Exception {
        // Get the reviewVector
        restReviewVectorMockMvc.perform(get("/api/review-vectors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReviewVector() throws Exception {
        // Initialize the database
        reviewVectorRepository.saveAndFlush(reviewVector);
        int databaseSizeBeforeUpdate = reviewVectorRepository.findAll().size();

        // Update the reviewVector
        ReviewVector updatedReviewVector = reviewVectorRepository.findOne(reviewVector.getId());
        updatedReviewVector
                .vector(UPDATED_VECTOR);
        ReviewVectorDTO reviewVectorDTO = reviewVectorMapper.reviewVectorToReviewVectorDTO(updatedReviewVector);

        restReviewVectorMockMvc.perform(put("/api/review-vectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewVectorDTO)))
            .andExpect(status().isOk());

        // Validate the ReviewVector in the database
        List<ReviewVector> reviewVectorList = reviewVectorRepository.findAll();
        assertThat(reviewVectorList).hasSize(databaseSizeBeforeUpdate);
        ReviewVector testReviewVector = reviewVectorList.get(reviewVectorList.size() - 1);
        assertThat(testReviewVector.getVector()).isEqualTo(UPDATED_VECTOR);
    }

    @Test
    @Transactional
    public void updateNonExistingReviewVector() throws Exception {
        int databaseSizeBeforeUpdate = reviewVectorRepository.findAll().size();

        // Create the ReviewVector
        ReviewVectorDTO reviewVectorDTO = reviewVectorMapper.reviewVectorToReviewVectorDTO(reviewVector);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReviewVectorMockMvc.perform(put("/api/review-vectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviewVectorDTO)))
            .andExpect(status().isCreated());

        // Validate the ReviewVector in the database
        List<ReviewVector> reviewVectorList = reviewVectorRepository.findAll();
        assertThat(reviewVectorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReviewVector() throws Exception {
        // Initialize the database
        reviewVectorRepository.saveAndFlush(reviewVector);
        int databaseSizeBeforeDelete = reviewVectorRepository.findAll().size();

        // Get the reviewVector
        restReviewVectorMockMvc.perform(delete("/api/review-vectors/{id}", reviewVector.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReviewVector> reviewVectorList = reviewVectorRepository.findAll();
        assertThat(reviewVectorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewVector.class);
    }
}
