package com.example.airportservice.models;

import com.example.airportservice.dto.SubscriptionDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "subscriptions")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Subscription implements Serializable {

    @Id
    @Column(name = "icao_code", columnDefinition = "text", unique = true, nullable = false, length = 4)
    private String icaoCode;

    @Column(name = "is_active", columnDefinition = "boolean default true", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_time", columnDefinition = "text")
    private Timestamp createdTime = new Timestamp(System.currentTimeMillis());
    ;

    @CreationTimestamp
    @Column(name = "last_modified_time", columnDefinition = "text")
    private Timestamp lastModifiedTime = new Timestamp(System.currentTimeMillis());

    public Subscription() {
        super();
    }

    public Subscription(SubscriptionDTO subscriptionDTO) {
        this.active = subscriptionDTO.getActive();
        this.icaoCode = subscriptionDTO.getIcaoCode();
    }


}

