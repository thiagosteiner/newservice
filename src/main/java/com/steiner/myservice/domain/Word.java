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
 * A Word.
 */
@Entity
@Table(name = "word")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "wordstring", nullable = false)
    private String wordstring;

    @OneToMany(mappedBy = "word")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WordRank> wordRanks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWordstring() {
        return wordstring;
    }

    public Word wordstring(String wordstring) {
        this.wordstring = wordstring;
        return this;
    }

    public void setWordstring(String wordstring) {
        this.wordstring = wordstring;
    }

    public Set<WordRank> getWordRanks() {
        return wordRanks;
    }

    public Word wordRanks(Set<WordRank> wordRanks) {
        this.wordRanks = wordRanks;
        return this;
    }

    public Word addWordRank(WordRank wordRank) {
        this.wordRanks.add(wordRank);
        wordRank.setWord(this);
        return this;
    }

    public Word removeWordRank(WordRank wordRank) {
        this.wordRanks.remove(wordRank);
        wordRank.setWord(null);
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
        Word word = (Word) o;
        if (word.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, word.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Word{" +
            "id=" + id +
            ", wordstring='" + wordstring + "'" +
            '}';
    }
}
