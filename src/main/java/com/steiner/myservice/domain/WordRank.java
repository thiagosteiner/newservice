package com.steiner.myservice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

/**
 * A WordRank.
 */
@Entity
@Transactional
@Table(name = "word_rank")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WordRank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "rank", nullable = false)
    private Integer rank;

    @ManyToOne
    private RankSnapshot rankSnapshot;

    @ManyToOne(optional = false)
    @NotNull
    private Word word;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public WordRank rank(Integer rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public RankSnapshot getRankSnapshot() {
        return rankSnapshot;
    }

    public WordRank rankSnapshot(RankSnapshot rankSnapshot) {
        this.rankSnapshot = rankSnapshot;
        return this;
    }

    public void setRankSnapshot(RankSnapshot rankSnapshot) {
        this.rankSnapshot = rankSnapshot;
    }

    public Word getWord() {
        return word;
    }

    public WordRank word(Word word) {
        this.word = word;
        return this;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WordRank wordRank = (WordRank) o;
        if (wordRank.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wordRank.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WordRank{" +
            "id=" + id +
            ", rank='" + rank + "'" +
            '}';
    }
}
