package com.steiner.myservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.steiner.myservice.domain.RankSnapshot;

import com.steiner.myservice.repository.RankSnapshotRepository;
import com.steiner.myservice.web.rest.util.HeaderUtil;
import com.steiner.myservice.service.dto.RankSnapshotDTO;
import com.steiner.myservice.service.mapper.RankSnapshotMapper;
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
 * REST controller for managing RankSnapshot.
 */
@RestController
@RequestMapping("/api")
public class RankSnapshotResource {

    private final Logger log = LoggerFactory.getLogger(RankSnapshotResource.class);

    private static final String ENTITY_NAME = "rankSnapshot";
        
    private final RankSnapshotRepository rankSnapshotRepository;

    private final RankSnapshotMapper rankSnapshotMapper;

    public RankSnapshotResource(RankSnapshotRepository rankSnapshotRepository, RankSnapshotMapper rankSnapshotMapper) {
        this.rankSnapshotRepository = rankSnapshotRepository;
        this.rankSnapshotMapper = rankSnapshotMapper;
    }

    /**
     * POST  /rank-snapshots : Create a new rankSnapshot.
     *
     * @param rankSnapshotDTO the rankSnapshotDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rankSnapshotDTO, or with status 400 (Bad Request) if the rankSnapshot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rank-snapshots")
    @Timed
    public ResponseEntity<RankSnapshotDTO> createRankSnapshot(@Valid @RequestBody RankSnapshotDTO rankSnapshotDTO) throws URISyntaxException {
        log.debug("REST request to save RankSnapshot : {}", rankSnapshotDTO);
        if (rankSnapshotDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new rankSnapshot cannot already have an ID")).body(null);
        }
        RankSnapshot rankSnapshot = rankSnapshotMapper.rankSnapshotDTOToRankSnapshot(rankSnapshotDTO);
        rankSnapshot = rankSnapshotRepository.save(rankSnapshot);
        RankSnapshotDTO result = rankSnapshotMapper.rankSnapshotToRankSnapshotDTO(rankSnapshot);
        return ResponseEntity.created(new URI("/api/rank-snapshots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rank-snapshots : Updates an existing rankSnapshot.
     *
     * @param rankSnapshotDTO the rankSnapshotDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rankSnapshotDTO,
     * or with status 400 (Bad Request) if the rankSnapshotDTO is not valid,
     * or with status 500 (Internal Server Error) if the rankSnapshotDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rank-snapshots")
    @Timed
    public ResponseEntity<RankSnapshotDTO> updateRankSnapshot(@Valid @RequestBody RankSnapshotDTO rankSnapshotDTO) throws URISyntaxException {
        log.debug("REST request to update RankSnapshot : {}", rankSnapshotDTO);
        if (rankSnapshotDTO.getId() == null) {
            return createRankSnapshot(rankSnapshotDTO);
        }
        RankSnapshot rankSnapshot = rankSnapshotMapper.rankSnapshotDTOToRankSnapshot(rankSnapshotDTO);
        rankSnapshot = rankSnapshotRepository.save(rankSnapshot);
        RankSnapshotDTO result = rankSnapshotMapper.rankSnapshotToRankSnapshotDTO(rankSnapshot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rankSnapshotDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rank-snapshots : get all the rankSnapshots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rankSnapshots in body
     */
    @GetMapping("/rank-snapshots")
    @Timed
    public List<RankSnapshotDTO> getAllRankSnapshots() {
        log.debug("REST request to get all RankSnapshots");
        List<RankSnapshot> rankSnapshots = rankSnapshotRepository.findAll();
        return rankSnapshotMapper.rankSnapshotsToRankSnapshotDTOs(rankSnapshots);
    }

    /**
     * GET  /rank-snapshots/:id : get the "id" rankSnapshot.
     *
     * @param id the id of the rankSnapshotDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rankSnapshotDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rank-snapshots/{id}")
    @Timed
    public ResponseEntity<RankSnapshotDTO> getRankSnapshot(@PathVariable Long id) {
        log.debug("REST request to get RankSnapshot : {}", id);
        RankSnapshot rankSnapshot = rankSnapshotRepository.findOne(id);
        RankSnapshotDTO rankSnapshotDTO = rankSnapshotMapper.rankSnapshotToRankSnapshotDTO(rankSnapshot);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rankSnapshotDTO));
    }

    /**
     * DELETE  /rank-snapshots/:id : delete the "id" rankSnapshot.
     *
     * @param id the id of the rankSnapshotDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rank-snapshots/{id}")
    @Timed
    public ResponseEntity<Void> deleteRankSnapshot(@PathVariable Long id) {
        log.debug("REST request to delete RankSnapshot : {}", id);
        rankSnapshotRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
