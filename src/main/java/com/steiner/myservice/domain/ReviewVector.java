package com.steiner.myservice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReviewVector.
 */
@Entity
@Table(name = "review_vector")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReviewVector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Lob
    @Column(name = "vector", nullable = false)
    private String vector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVector() {
        return vector;
    }

    public ReviewVector vector(String vector) {
        this.vector = vector;
        return this;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewVector reviewVector = (ReviewVector) o;
        if (reviewVector.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reviewVector.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReviewVector{" +
            "id=" + id +
            ", vector='" + vector + "'" +
            '}';
    }
}
