package com.example.airportservice.models;

import com.example.airportservice.dto.MetarDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "metar")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Metar implements Serializable {

    @Id
    @Column(name = "icao_code", columnDefinition = "text", unique = true, nullable = false, length = 4)
    private String icaoCode;

    @Lob
    @Column(name = "data", columnDefinition = "text", nullable = false)
    private String data;

    @CreationTimestamp
    @Column(name = "CREATED_TIME", columnDefinition = "text")
    private Timestamp createdTime = new Timestamp(System.currentTimeMillis());

    @CreationTimestamp
    @Column(name = "LAST_MODIFIED_TIME", columnDefinition = "text")
    private Timestamp lastModifiedTime = new Timestamp(System.currentTimeMillis());

    public Metar() {
        super();
    }

    public Metar(MetarDTO metarDTO) {
        this.icaoCode = metarDTO.getIcaoCode();
        this.data = metarDTO.getData();
    }
    
}
