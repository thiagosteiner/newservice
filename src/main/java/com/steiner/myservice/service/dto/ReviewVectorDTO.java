package com.steiner.myservice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ReviewVector entity.
 */
public class ReviewVectorDTO implements Serializable {

    private Long id;

    @NotNull
    @Lob
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

        ReviewVectorDTO reviewVectorDTO = (ReviewVectorDTO) o;

        if ( ! Objects.equals(id, reviewVectorDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReviewVectorDTO{" +
            "id=" + id +
            ", vector='" + vector + "'" +
            '}';
    }
}
