package com.steiner.myservice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the WordRank entity.
 */
public class WordRankDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer rank;

    private Long rankSnapshotId;

    private Long wordId;

    private String wordWordstring;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Long getRankSnapshotId() {
        return rankSnapshotId;
    }

    public void setRankSnapshotId(Long rankSnapshotId) {
        this.rankSnapshotId = rankSnapshotId;
    }

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getWordWordstring() {
        return wordWordstring;
    }

    public void setWordWordstring(String wordWordstring) {
        this.wordWordstring = wordWordstring;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WordRankDTO wordRankDTO = (WordRankDTO) o;

        if ( ! Objects.equals(id, wordRankDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WordRankDTO{" +
            "id=" + id +
            ", rank='" + rank + "'" +
            '}';
    }
}
