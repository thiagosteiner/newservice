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
 * A Review.
 */
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "reviewstring", nullable = false)
    private String reviewstring;

    @NotNull
    @Lob
    @Column(name = "reviewtext", nullable = false)
    private String reviewtext;

    @OneToMany(mappedBy = "review")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WordOccurrences> wordOccurrences = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Book book;

    @OneToOne
    @JoinColumn(unique = true)
    private ReviewVector reviewVector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewstring() {
        return reviewstring;
    }

    public Review reviewstring(String reviewstring) {
        this.reviewstring = reviewstring;
        return this;
    }

    public void setReviewstring(String reviewstring) {
        this.reviewstring = reviewstring;
    }

    public String getReviewtext() {
        return reviewtext;
    }

    public Review reviewtext(String reviewtext) {
        this.reviewtext = reviewtext;
        return this;
    }

    public void setReviewtext(String reviewtext) {
        this.reviewtext = reviewtext;
    }

    public Set<WordOccurrences> getWordOccurrences() {
        return wordOccurrences;
    }

    public Review wordOccurrences(Set<WordOccurrences> wordOccurrences) {
        this.wordOccurrences = wordOccurrences;
        return this;
    }

    public Review addWordOccurrences(WordOccurrences wordOccurrences) {
        this.wordOccurrences.add(wordOccurrences);
        wordOccurrences.setReview(this);
        return this;
    }

    public Review removeWordOccurrences(WordOccurrences wordOccurrences) {
        this.wordOccurrences.remove(wordOccurrences);
        wordOccurrences.setReview(null);
        return this;
    }

    public void setWordOccurrences(Set<WordOccurrences> wordOccurrences) {
        this.wordOccurrences = wordOccurrences;
    }

    public Book getBook() {
        return book;
    }

    public Review book(Book book) {
        this.book = book;
        return this;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public ReviewVector getReviewVector() {
        return reviewVector;
    }

    public Review reviewVector(ReviewVector reviewVector) {
        this.reviewVector = reviewVector;
        return this;
    }

    public void setReviewVector(ReviewVector reviewVector) {
        this.reviewVector = reviewVector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Review review = (Review) o;
        if (review.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Review{" +
            "id=" + id +
            ", reviewstring='" + reviewstring + "'" +
            ", reviewtext='" + reviewtext + "'" +
            '}';
    }
}
