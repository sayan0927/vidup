package com.example.vidupcoremodule.core.entity.DatabaseEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Table(name = "content_chunk")
@Entity
public class ContentChunk {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "content_id")
    UUID contentId;

    @Column(name = "contentchunk_sequence")
    Integer sequence;

    @Column(name = "size")
    Integer size;

    @JsonIgnore
    @Column(name = "chunk_data")
    byte[] chunkData;

    @Override
    public String toString() {
        return id+","+contentId+","+sequence+","+size;
    }
}
