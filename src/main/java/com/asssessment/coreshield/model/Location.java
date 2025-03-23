package com.asssessment.coreshield.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
public class Location {
    @Id
    private String id;
    private double latitude;
    private double longitude;

    public Location() {
    }

    @JsonCreator
    public Location(@JsonProperty("id") String id,@JsonProperty("latitude") double latitude,@JsonProperty("longitude") double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
