package com.example.airportservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "station")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Station implements Serializable {

    @Id
    @Column(name = "icao_code", columnDefinition = "text", unique = true, nullable = false, length = 4)
    private String icaoCode;

    @Column(name = "filename", columnDefinition = "text", unique = true, nullable = false, length = 8)
    private String filename;

    @Column(name = "last_modified", columnDefinition = "text", nullable = false)
    private String lastModified;

    @Column(name = "size", columnDefinition = "int", nullable = false)
    private int size;

    public Station() {
        super();
    }

}
