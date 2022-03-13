package com.example.airportservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SubscriptionDTO implements Serializable {
    private String icaoCode;
    private Boolean active = true;
    private Timestamp createdTime;
    private Timestamp lastModifiedTime;

    public SubscriptionDTO() {
        super();
    }


}
