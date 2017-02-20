package com.steiner.myservice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

/**
 * A WordOccurrences.
 */
@Entity
@Table(name = "word_occurrences")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Transactional
public class WordOccurrences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "amountoccurrences", nullable = false)
    private Integer amountoccurrences;

    @NotNull
    @Column(name = "word", nullable = false)
    private String word;

    @ManyToOne(optional = false)
    @NotNull
    private Review review;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmountoccurrences() {
        return amountoccurrences;
    }

    public WordOccurrences amountoccurrences(Integer amountoccurrences) {
        this.amountoccurrences = amountoccurrences;
        return this;
    }

    public void setAmountoccurrences(Integer amountoccurrences) {
        this.amountoccurrences = amountoccurrences;
    }

    public String getWord() {
        return word;
    }

    public WordOccurrences word(String word) {
        this.word = word;
        return this;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Review getReview() {
        return review;
    }

    public WordOccurrences review(Review review) {
        this.review = review;
        return this;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WordOccurrences wordOccurrences = (WordOccurrences) o;
        if (wordOccurrences.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wordOccurrences.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WordOccurrences{" +
            "id=" + id +
            ", amountoccurrences='" + amountoccurrences + "'" +
            ", word='" + word + "'" +
            '}';
    }
}
