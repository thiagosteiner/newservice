package com.steiner.myservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RankSnapshot.
 */
@Entity
@Table(name = "rank_snapshot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RankSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "epoch", nullable = false)
    private Long epoch;

    @OneToMany(mappedBy = "rankSnapshot")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WordRank> wordRanks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEpoch() {
        return epoch;
    }

    public RankSnapshot epoch(Long epoch) {
        this.epoch = epoch;
        return this;
    }

    public void setEpoch(Long epoch) {
        this.epoch = epoch;
    }

    public Set<WordRank> getWordRanks() {
        return wordRanks;
    }

    public RankSnapshot wordRanks(Set<WordRank> wordRanks) {
        this.wordRanks = wordRanks;
        return this;
    }

    public RankSnapshot addWordRank(WordRank wordRank) {
        this.wordRanks.add(wordRank);
        wordRank.setRankSnapshot(this);
        return this;
    }

    public RankSnapshot removeWordRank(WordRank wordRank) {
        this.wordRanks.remove(wordRank);
        wordRank.setRankSnapshot(null);
        return this;
    }

    public void setWordRanks(Set<WordRank> wordRanks) {
        this.wordRanks = wordRanks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RankSnapshot rankSnapshot = (RankSnapshot) o;
        if (rankSnapshot.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rankSnapshot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RankSnapshot{" +
            "id=" + id +
            ", epoch='" + epoch + "'" +
            '}';
    }
}
