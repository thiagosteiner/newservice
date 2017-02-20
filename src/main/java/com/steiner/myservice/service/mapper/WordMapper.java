package com.steiner.myservice.service.mapper;

import com.steiner.myservice.domain.*;
import com.steiner.myservice.service.dto.WordDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Word and its DTO WordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WordMapper {

    WordDTO wordToWordDTO(Word word);

    List<WordDTO> wordsToWordDTOs(List<Word> words);

    @Mapping(target = "wordRanks", ignore = true)
    Word wordDTOToWord(WordDTO wordDTO);

    List<Word> wordDTOsToWords(List<WordDTO> wordDTOs);
}
