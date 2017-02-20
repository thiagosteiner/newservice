package com.steiner.myservice.service.mapper;

import com.steiner.myservice.domain.*;
import com.steiner.myservice.service.dto.WordRankDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WordRank and its DTO WordRankDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WordRankMapper {

    @Mapping(source = "rankSnapshot.id", target = "rankSnapshotId")
    @Mapping(source = "word.id", target = "wordId")
    @Mapping(source = "word.wordstring", target = "wordWordstring")
    WordRankDTO wordRankToWordRankDTO(WordRank wordRank);

    List<WordRankDTO> wordRanksToWordRankDTOs(List<WordRank> wordRanks);

    @Mapping(source = "rankSnapshotId", target = "rankSnapshot")
    @Mapping(source = "wordId", target = "word")
    WordRank wordRankDTOToWordRank(WordRankDTO wordRankDTO);

    List<WordRank> wordRankDTOsToWordRanks(List<WordRankDTO> wordRankDTOs);

    default RankSnapshot rankSnapshotFromId(Long id) {
        if (id == null) {
            return null;
        }
        RankSnapshot rankSnapshot = new RankSnapshot();
        rankSnapshot.setId(id);
        return rankSnapshot;
    }

    default Word wordFromId(Long id) {
        if (id == null) {
            return null;
        }
        Word word = new Word();
        word.setId(id);
        return word;
    }
}
