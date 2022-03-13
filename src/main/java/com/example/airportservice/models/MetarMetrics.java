package com.example.airportservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "metar_metrics")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MetarMetrics implements Serializable {

    @Id
    @Column(name = "icao_code", columnDefinition = "text", unique = true, nullable = false, length = 4)
    private String icaoCode;

    @Column(name = "timestamp", columnDefinition = "text")
    private Timestamp timestamp;

    @Column(name = "wind_strength", columnDefinition = "text")
    private String windStrength;

    @Column(name = "temperature", columnDefinition = "text")
    private String temperature;

    @Column(name = "visibility", columnDefinition = "text")
    private String visibility;

    public MetarMetrics() {
        super();
    }

}