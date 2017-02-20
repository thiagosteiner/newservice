package com.steiner.myservice.web.rest;

import com.steiner.myservice.MyserviceApp;

import com.steiner.myservice.domain.WordRank;
import com.steiner.myservice.domain.Word;
import com.steiner.myservice.repository.WordRankRepository;
import com.steiner.myservice.service.dto.WordRankDTO;
import com.steiner.myservice.service.mapper.WordRankMapper;
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
 * Test class for the WordRankResource REST controller.
 *
 * @see WordRankResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyserviceApp.class)
@Transactional
public class WordRankResourceIntTest {

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;

    @Autowired
    private WordRankRepository wordRankRepository;

    @Autowired
    private WordRankMapper wordRankMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWordRankMockMvc;

    private WordRank wordRank;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            WordRankResource wordRankResource = new WordRankResource(wordRankRepository, wordRankMapper);
        this.restWordRankMockMvc = MockMvcBuilders.standaloneSetup(wordRankResource)
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
    public static WordRank createEntity(EntityManager em) {
        WordRank wordRank = new WordRank()
                .rank(DEFAULT_RANK);
        // Add required entity
        Word word = WordResourceIntTest.createEntity(em);
        em.persist(word);
        em.flush();
        wordRank.setWord(word);
        return wordRank;
    }

    @Before
    public void initTest() {
        wordRank = createEntity(em);
    }

    @Test
    @Transactional
    public void createWordRank() throws Exception {
        int databaseSizeBeforeCreate = wordRankRepository.findAll().size();

        // Create the WordRank
        WordRankDTO wordRankDTO = wordRankMapper.wordRankToWordRankDTO(wordRank);

        restWordRankMockMvc.perform(post("/api/word-ranks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordRankDTO)))
            .andExpect(status().isCreated());

        // Validate the WordRank in the database
        List<WordRank> wordRankList = wordRankRepository.findAll();
        assertThat(wordRankList).hasSize(databaseSizeBeforeCreate + 1);
        WordRank testWordRank = wordRankList.get(wordRankList.size() - 1);
        assertThat(testWordRank.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    public void createWordRankWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wordRankRepository.findAll().size();

        // Create the WordRank with an existing ID
        WordRank existingWordRank = new WordRank();
        existingWordRank.setId(1L);
        WordRankDTO existingWordRankDTO = wordRankMapper.wordRankToWordRankDTO(existingWordRank);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordRankMockMvc.perform(post("/api/word-ranks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWordRankDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WordRank> wordRankList = wordRankRepository.findAll();
        assertThat(wordRankList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRankIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordRankRepository.findAll().size();
        // set the field null
        wordRank.setRank(null);

        // Create the WordRank, which fails.
        WordRankDTO wordRankDTO = wordRankMapper.wordRankToWordRankDTO(wordRank);

        restWordRankMockMvc.perform(post("/api/word-ranks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordRankDTO)))
            .andExpect(status().isBadRequest());

        List<WordRank> wordRankList = wordRankRepository.findAll();
        assertThat(wordRankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWordRanks() throws Exception {
        // Initialize the database
        wordRankRepository.saveAndFlush(wordRank);

        // Get all the wordRankList
        restWordRankMockMvc.perform(get("/api/word-ranks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordRank.getId().intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)));
    }

    @Test
    @Transactional
    public void getWordRank() throws Exception {
        // Initialize the database
        wordRankRepository.saveAndFlush(wordRank);

        // Get the wordRank
        restWordRankMockMvc.perform(get("/api/word-ranks/{id}", wordRank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wordRank.getId().intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK));
    }

    @Test
    @Transactional
    public void getNonExistingWordRank() throws Exception {
        // Get the wordRank
        restWordRankMockMvc.perform(get("/api/word-ranks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWordRank() throws Exception {
        // Initialize the database
        wordRankRepository.saveAndFlush(wordRank);
        int databaseSizeBeforeUpdate = wordRankRepository.findAll().size();

        // Update the wordRank
        WordRank updatedWordRank = wordRankRepository.findOne(wordRank.getId());
        updatedWordRank
                .rank(UPDATED_RANK);
        WordRankDTO wordRankDTO = wordRankMapper.wordRankToWordRankDTO(updatedWordRank);

        restWordRankMockMvc.perform(put("/api/word-ranks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordRankDTO)))
            .andExpect(status().isOk());

        // Validate the WordRank in the database
        List<WordRank> wordRankList = wordRankRepository.findAll();
        assertThat(wordRankList).hasSize(databaseSizeBeforeUpdate);
        WordRank testWordRank = wordRankList.get(wordRankList.size() - 1);
        assertThat(testWordRank.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    public void updateNonExistingWordRank() throws Exception {
        int databaseSizeBeforeUpdate = wordRankRepository.findAll().size();

        // Create the WordRank
        WordRankDTO wordRankDTO = wordRankMapper.wordRankToWordRankDTO(wordRank);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWordRankMockMvc.perform(put("/api/word-ranks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordRankDTO)))
            .andExpect(status().isCreated());

        // Validate the WordRank in the database
        List<WordRank> wordRankList = wordRankRepository.findAll();
        assertThat(wordRankList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWordRank() throws Exception {
        // Initialize the database
        wordRankRepository.saveAndFlush(wordRank);
        int databaseSizeBeforeDelete = wordRankRepository.findAll().size();

        // Get the wordRank
        restWordRankMockMvc.perform(delete("/api/word-ranks/{id}", wordRank.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WordRank> wordRankList = wordRankRepository.findAll();
        assertThat(wordRankList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordRank.class);
    }
}
