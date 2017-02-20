package com.steiner.myservice.service.mapper;

import com.steiner.myservice.domain.*;
import com.steiner.myservice.service.dto.RankSnapshotDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity RankSnapshot and its DTO RankSnapshotDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RankSnapshotMapper {

    RankSnapshotDTO rankSnapshotToRankSnapshotDTO(RankSnapshot rankSnapshot);

    List<RankSnapshotDTO> rankSnapshotsToRankSnapshotDTOs(List<RankSnapshot> rankSnapshots);

    @Mapping(target = "wordRanks", ignore = true)
    RankSnapshot rankSnapshotDTOToRankSnapshot(RankSnapshotDTO rankSnapshotDTO);

    List<RankSnapshot> rankSnapshotDTOsToRankSnapshots(List<RankSnapshotDTO> rankSnapshotDTOs);
}
