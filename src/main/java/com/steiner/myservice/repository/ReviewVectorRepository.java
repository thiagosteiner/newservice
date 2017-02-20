package com.steiner.myservice.repository;

import com.steiner.myservice.domain.ReviewVector;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReviewVector entity.
 */
@SuppressWarnings("unused")
public interface ReviewVectorRepository extends JpaRepository<ReviewVector,Long> {

}
