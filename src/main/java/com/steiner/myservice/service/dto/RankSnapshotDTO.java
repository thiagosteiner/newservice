package com.steiner.myservice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the RankSnapshot entity.
 */
public class RankSnapshotDTO implements Serializable {

    private Long id;

    @NotNull
    private Long epoch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getEpoch() {
        return epoch;
    }

    public void setEpoch(Long epoch) {
        this.epoch = epoch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RankSnapshotDTO rankSnapshotDTO = (RankSnapshotDTO) o;

        if ( ! Objects.equals(id, rankSnapshotDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RankSnapshotDTO{" +
            "id=" + id +
            ", epoch='" + epoch + "'" +
            '}';
    }
}
