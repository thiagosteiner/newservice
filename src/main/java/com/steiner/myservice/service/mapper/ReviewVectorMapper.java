package com.steiner.myservice.service.mapper;

import com.steiner.myservice.domain.*;
import com.steiner.myservice.service.dto.ReviewVectorDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ReviewVector and its DTO ReviewVectorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReviewVectorMapper {

    ReviewVectorDTO reviewVectorToReviewVectorDTO(ReviewVector reviewVector);

    List<ReviewVectorDTO> reviewVectorsToReviewVectorDTOs(List<ReviewVector> reviewVectors);

    ReviewVector reviewVectorDTOToReviewVector(ReviewVectorDTO reviewVectorDTO);

    List<ReviewVector> reviewVectorDTOsToReviewVectors(List<ReviewVectorDTO> reviewVectorDTOs);
}
