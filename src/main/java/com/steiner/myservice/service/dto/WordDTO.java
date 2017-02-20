package com.steiner.myservice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Word entity.
 */
public class WordDTO implements Serializable {

    private Long id;

    @NotNull
    private String wordstring;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getWordstring() {
        return wordstring;
    }

    public void setWordstring(String wordstring) {
        this.wordstring = wordstring;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WordDTO wordDTO = (WordDTO) o;

        if ( ! Objects.equals(id, wordDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WordDTO{" +
            "id=" + id +
            ", wordstring='" + wordstring + "'" +
            '}';
    }
}
