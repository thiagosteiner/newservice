package com.steiner.myservice.repository;

import com.steiner.myservice.domain.RankSnapshot;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RankSnapshot entity.
 */
@SuppressWarnings("unused")
public interface RankSnapshotRepository extends JpaRepository<RankSnapshot,Long> {

}
