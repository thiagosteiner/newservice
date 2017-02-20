package com.steiner.myservice.web.rest;

import com.steiner.myservice.MyserviceApp;

import com.steiner.myservice.domain.RankSnapshot;
import com.steiner.myservice.repository.RankSnapshotRepository;
import com.steiner.myservice.service.dto.RankSnapshotDTO;
import com.steiner.myservice.service.mapper.RankSnapshotMapper;
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
 * Test class for the RankSnapshotResource REST controller.
 *
 * @see RankSnapshotResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyserviceApp.class)
public class RankSnapshotResourceIntTest {

    private static final Long DEFAULT_EPOCH = 1L;
    private static final Long UPDATED_EPOCH = 2L;

    @Autowired
    private RankSnapshotRepository rankSnapshotRepository;

    @Autowired
    private RankSnapshotMapper rankSnapshotMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRankSnapshotMockMvc;

    private RankSnapshot rankSnapshot;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            RankSnapshotResource rankSnapshotResource = new RankSnapshotResource(rankSnapshotRepository, rankSnapshotMapper);
        this.restRankSnapshotMockMvc = MockMvcBuilders.standaloneSetup(rankSnapshotResource)
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
    public static RankSnapshot createEntity(EntityManager em) {
        RankSnapshot rankSnapshot = new RankSnapshot()
                .epoch(DEFAULT_EPOCH);
        return rankSnapshot;
    }

    @Before
    public void initTest() {
        rankSnapshot = createEntity(em);
    }

    @Test
    @Transactional
    public void createRankSnapshot() throws Exception {
        int databaseSizeBeforeCreate = rankSnapshotRepository.findAll().size();

        // Create the RankSnapshot
        RankSnapshotDTO rankSnapshotDTO = rankSnapshotMapper.rankSnapshotToRankSnapshotDTO(rankSnapshot);

        restRankSnapshotMockMvc.perform(post("/api/rank-snapshots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rankSnapshotDTO)))
            .andExpect(status().isCreated());

        // Validate the RankSnapshot in the database
        List<RankSnapshot> rankSnapshotList = rankSnapshotRepository.findAll();
        assertThat(rankSnapshotList).hasSize(databaseSizeBeforeCreate + 1);
        RankSnapshot testRankSnapshot = rankSnapshotList.get(rankSnapshotList.size() - 1);
        assertThat(testRankSnapshot.getEpoch()).isEqualTo(DEFAULT_EPOCH);
    }

    @Test
    @Transactional
    public void createRankSnapshotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rankSnapshotRepository.findAll().size();

        // Create the RankSnapshot with an existing ID
        RankSnapshot existingRankSnapshot = new RankSnapshot();
        existingRankSnapshot.setId(1L);
        RankSnapshotDTO existingRankSnapshotDTO = rankSnapshotMapper.rankSnapshotToRankSnapshotDTO(existingRankSnapshot);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRankSnapshotMockMvc.perform(post("/api/rank-snapshots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRankSnapshotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RankSnapshot> rankSnapshotList = rankSnapshotRepository.findAll();
        assertThat(rankSnapshotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEpochIsRequired() throws Exception {
        int databaseSizeBeforeTest = rankSnapshotRepository.findAll().size();
        // set the field null
        rankSnapshot.setEpoch(null);

        // Create the RankSnapshot, which fails.
        RankSnapshotDTO rankSnapshotDTO = rankSnapshotMapper.rankSnapshotToRankSnapshotDTO(rankSnapshot);

        restRankSnapshotMockMvc.perform(post("/api/rank-snapshots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rankSnapshotDTO)))
            .andExpect(status().isBadRequest());

        List<RankSnapshot> rankSnapshotList = rankSnapshotRepository.findAll();
        assertThat(rankSnapshotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRankSnapshots() throws Exception {
        // Initialize the database
        rankSnapshotRepository.saveAndFlush(rankSnapshot);

        // Get all the rankSnapshotList
        restRankSnapshotMockMvc.perform(get("/api/rank-snapshots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rankSnapshot.getId().intValue())))
            .andExpect(jsonPath("$.[*].epoch").value(hasItem(DEFAULT_EPOCH.intValue())));
    }

    @Test
    @Transactional
    public void getRankSnapshot() throws Exception {
        // Initialize the database
        rankSnapshotRepository.saveAndFlush(rankSnapshot);

        // Get the rankSnapshot
        restRankSnapshotMockMvc.perform(get("/api/rank-snapshots/{id}", rankSnapshot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rankSnapshot.getId().intValue()))
            .andExpect(jsonPath("$.epoch").value(DEFAULT_EPOCH.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRankSnapshot() throws Exception {
        // Get the rankSnapshot
        restRankSnapshotMockMvc.perform(get("/api/rank-snapshots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRankSnapshot() throws Exception {
        // Initialize the database
        rankSnapshotRepository.saveAndFlush(rankSnapshot);
        int databaseSizeBeforeUpdate = rankSnapshotRepository.findAll().size();

        // Update the rankSnapshot
        RankSnapshot updatedRankSnapshot = rankSnapshotRepository.findOne(rankSnapshot.getId());
        updatedRankSnapshot
                .epoch(UPDATED_EPOCH);
        RankSnapshotDTO rankSnapshotDTO = rankSnapshotMapper.rankSnapshotToRankSnapshotDTO(updatedRankSnapshot);

        restRankSnapshotMockMvc.perform(put("/api/rank-snapshots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rankSnapshotDTO)))
            .andExpect(status().isOk());

        // Validate the RankSnapshot in the database
        List<RankSnapshot> rankSnapshotList = rankSnapshotRepository.findAll();
        assertThat(rankSnapshotList).hasSize(databaseSizeBeforeUpdate);
        RankSnapshot testRankSnapshot = rankSnapshotList.get(rankSnapshotList.size() - 1);
        assertThat(testRankSnapshot.getEpoch()).isEqualTo(UPDATED_EPOCH);
    }

    @Test
    @Transactional
    public void updateNonExistingRankSnapshot() throws Exception {
        int databaseSizeBeforeUpdate = rankSnapshotRepository.findAll().size();

        // Create the RankSnapshot
        RankSnapshotDTO rankSnapshotDTO = rankSnapshotMapper.rankSnapshotToRankSnapshotDTO(rankSnapshot);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRankSnapshotMockMvc.perform(put("/api/rank-snapshots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rankSnapshotDTO)))
            .andExpect(status().isCreated());

        // Validate the RankSnapshot in the database
        List<RankSnapshot> rankSnapshotList = rankSnapshotRepository.findAll();
        assertThat(rankSnapshotList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRankSnapshot() throws Exception {
        // Initialize the database
        rankSnapshotRepository.saveAndFlush(rankSnapshot);
        int databaseSizeBeforeDelete = rankSnapshotRepository.findAll().size();

        // Get the rankSnapshot
        restRankSnapshotMockMvc.perform(delete("/api/rank-snapshots/{id}", rankSnapshot.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RankSnapshot> rankSnapshotList = rankSnapshotRepository.findAll();
        assertThat(rankSnapshotList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RankSnapshot.class);
    }
}
