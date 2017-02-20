package com.steiner.myservice.web.rest;

import com.steiner.myservice.MyserviceApp;

import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.service.dto.WordOccurrencesDTO;
import com.steiner.myservice.service.mapper.WordOccurrencesMapper;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WordOccurrencesResource REST controller.
 *
 * @see WordOccurrencesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyserviceApp.class)
@Transactional
public class WordOccurrencesResourceIntTest {

    private static final Integer DEFAULT_AMOUNTOCCURRENCES = 1;
    private static final Integer UPDATED_AMOUNTOCCURRENCES = 2;

    private static final String DEFAULT_WORD = "AAAAAAAAAA";
    private static final String UPDATED_WORD = "BBBBBBBBBB";

    @Autowired
    private WordOccurrencesRepository wordOccurrencesRepository;

    @Autowired
    private WordOccurrencesMapper wordOccurrencesMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWordOccurrencesMockMvc;

    private WordOccurrences wordOccurrences;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            WordOccurrencesResource wordOccurrencesResource = new WordOccurrencesResource(wordOccurrencesRepository, wordOccurrencesMapper);
        this.restWordOccurrencesMockMvc = MockMvcBuilders.standaloneSetup(wordOccurrencesResource)
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
    public static WordOccurrences createEntity(EntityManager em) {
        WordOccurrences wordOccurrences = new WordOccurrences()
                .amountoccurrences(DEFAULT_AMOUNTOCCURRENCES)
                .word(DEFAULT_WORD);
        // Add required entity
        Review review = ReviewResourceIntTest.createEntity(em);
        em.persist(review);
        em.flush();
        wordOccurrences.setReview(review);
        return wordOccurrences;
    }

    @Before
    public void initTest() {
        wordOccurrences = createEntity(em);
    }

    @Test
    @Transactional
    public void createWordOccurrences() throws Exception {
        int databaseSizeBeforeCreate = wordOccurrencesRepository.findAll().size();

        // Create the WordOccurrences
        WordOccurrencesDTO wordOccurrencesDTO = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(wordOccurrences);

        restWordOccurrencesMockMvc.perform(post("/api/word-occurrences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordOccurrencesDTO)))
            .andExpect(status().isCreated());

        // Validate the WordOccurrences in the database
        List<WordOccurrences> wordOccurrencesList = wordOccurrencesRepository.findAll();
        assertThat(wordOccurrencesList).hasSize(databaseSizeBeforeCreate + 1);
        WordOccurrences testWordOccurrences = wordOccurrencesList.get(wordOccurrencesList.size() - 1);
        assertThat(testWordOccurrences.getAmountoccurrences()).isEqualTo(DEFAULT_AMOUNTOCCURRENCES);
        assertThat(testWordOccurrences.getWord()).isEqualTo(DEFAULT_WORD);
    }

    @Test
    @Transactional
    public void createWordOccurrencesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wordOccurrencesRepository.findAll().size();

        // Create the WordOccurrences with an existing ID
        WordOccurrences existingWordOccurrences = new WordOccurrences();
        existingWordOccurrences.setId(1L);
        WordOccurrencesDTO existingWordOccurrencesDTO = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(existingWordOccurrences);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordOccurrencesMockMvc.perform(post("/api/word-occurrences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWordOccurrencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WordOccurrences> wordOccurrencesList = wordOccurrencesRepository.findAll();
        assertThat(wordOccurrencesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountoccurrencesIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordOccurrencesRepository.findAll().size();
        // set the field null
        wordOccurrences.setAmountoccurrences(null);

        // Create the WordOccurrences, which fails.
        WordOccurrencesDTO wordOccurrencesDTO = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(wordOccurrences);

        restWordOccurrencesMockMvc.perform(post("/api/word-occurrences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordOccurrencesDTO)))
            .andExpect(status().isBadRequest());

        List<WordOccurrences> wordOccurrencesList = wordOccurrencesRepository.findAll();
        assertThat(wordOccurrencesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWordIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordOccurrencesRepository.findAll().size();
        // set the field null
        wordOccurrences.setWord(null);

        // Create the WordOccurrences, which fails.
        WordOccurrencesDTO wordOccurrencesDTO = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(wordOccurrences);

        restWordOccurrencesMockMvc.perform(post("/api/word-occurrences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordOccurrencesDTO)))
            .andExpect(status().isBadRequest());

        List<WordOccurrences> wordOccurrencesList = wordOccurrencesRepository.findAll();
        assertThat(wordOccurrencesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWordOccurrences() throws Exception {
        // Initialize the database
        wordOccurrencesRepository.saveAndFlush(wordOccurrences);

        // Get all the wordOccurrencesList
        restWordOccurrencesMockMvc.perform(get("/api/word-occurrences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordOccurrences.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountoccurrences").value(hasItem(DEFAULT_AMOUNTOCCURRENCES)))
            .andExpect(jsonPath("$.[*].word").value(hasItem(DEFAULT_WORD.toString())));
    }

    @Test
    @Transactional
    public void getWordOccurrences() throws Exception {
        // Initialize the database
        wordOccurrencesRepository.saveAndFlush(wordOccurrences);

        // Get the wordOccurrences
        restWordOccurrencesMockMvc.perform(get("/api/word-occurrences/{id}", wordOccurrences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wordOccurrences.getId().intValue()))
            .andExpect(jsonPath("$.amountoccurrences").value(DEFAULT_AMOUNTOCCURRENCES))
            .andExpect(jsonPath("$.word").value(DEFAULT_WORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWordOccurrences() throws Exception {
        // Get the wordOccurrences
        restWordOccurrencesMockMvc.perform(get("/api/word-occurrences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWordOccurrences() throws Exception {
        // Initialize the database
        wordOccurrencesRepository.saveAndFlush(wordOccurrences);
        int databaseSizeBeforeUpdate = wordOccurrencesRepository.findAll().size();

        // Update the wordOccurrences
        WordOccurrences updatedWordOccurrences = wordOccurrencesRepository.findOne(wordOccurrences.getId());
        updatedWordOccurrences
                .amountoccurrences(UPDATED_AMOUNTOCCURRENCES)
                .word(UPDATED_WORD);
        WordOccurrencesDTO wordOccurrencesDTO = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(updatedWordOccurrences);

        restWordOccurrencesMockMvc.perform(put("/api/word-occurrences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordOccurrencesDTO)))
            .andExpect(status().isOk());

        // Validate the WordOccurrences in the database
        List<WordOccurrences> wordOccurrencesList = wordOccurrencesRepository.findAll();
        assertThat(wordOccurrencesList).hasSize(databaseSizeBeforeUpdate);
        WordOccurrences testWordOccurrences = wordOccurrencesList.get(wordOccurrencesList.size() - 1);
        assertThat(testWordOccurrences.getAmountoccurrences()).isEqualTo(UPDATED_AMOUNTOCCURRENCES);
        assertThat(testWordOccurrences.getWord()).isEqualTo(UPDATED_WORD);
    }

    @Test
    @Transactional
    public void updateNonExistingWordOccurrences() throws Exception {
        int databaseSizeBeforeUpdate = wordOccurrencesRepository.findAll().size();

        // Create the WordOccurrences
        WordOccurrencesDTO wordOccurrencesDTO = wordOccurrencesMapper.wordOccurrencesToWordOccurrencesDTO(wordOccurrences);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWordOccurrencesMockMvc.perform(put("/api/word-occurrences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordOccurrencesDTO)))
            .andExpect(status().isCreated());

        // Validate the WordOccurrences in the database
        List<WordOccurrences> wordOccurrencesList = wordOccurrencesRepository.findAll();
        assertThat(wordOccurrencesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWordOccurrences() throws Exception {
        // Initialize the database
        wordOccurrencesRepository.saveAndFlush(wordOccurrences);
        int databaseSizeBeforeDelete = wordOccurrencesRepository.findAll().size();

        // Get the wordOccurrences
        restWordOccurrencesMockMvc.perform(delete("/api/word-occurrences/{id}", wordOccurrences.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WordOccurrences> wordOccurrencesList = wordOccurrencesRepository.findAll();
        assertThat(wordOccurrencesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordOccurrences.class);
    }
}
