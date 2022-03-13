package com.example.airportservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MetarDTO implements Serializable {

    /**
     * Station code / icao code
     */
    private String icaoCode;
    /**
     * Metar Data
     */
    private String data;
    /**
     * Created Time
     */
    private Timestamp createdTime;
    /**
     * Last Modified Time
     */
    private Timestamp lastModifiedTime;

    public MetarDTO() {
        super();
    }

}
