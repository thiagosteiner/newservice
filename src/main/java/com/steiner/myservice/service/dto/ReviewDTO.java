package com.steiner.myservice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Review entity.
 */
public class ReviewDTO implements Serializable {

    private Long id;

    @NotNull
    private String reviewstring;

    @NotNull
    @Lob
    private String reviewtext;

    private Long bookId;

    private Long reviewVectorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getReviewstring() {
        return reviewstring;
    }

    public void setReviewstring(String reviewstring) {
        this.reviewstring = reviewstring;
    }
    public String getReviewtext() {
        return reviewtext;
    }

    public void setReviewtext(String reviewtext) {
        this.reviewtext = reviewtext;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getReviewVectorId() {
        return reviewVectorId;
    }

    public void setReviewVectorId(Long reviewVectorId) {
        this.reviewVectorId = reviewVectorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReviewDTO reviewDTO = (ReviewDTO) o;

        if ( ! Objects.equals(id, reviewDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
            "id=" + id +
            ", reviewstring='" + reviewstring + "'" +
            ", reviewtext='" + reviewtext + "'" +
            '}';
    }
}
