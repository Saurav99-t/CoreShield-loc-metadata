package com.asssessment.coreshield.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
public class Metadata {
    @Id
    private String id;
    private String type;
    private double rating;
    private int reviews;

    public Metadata() {
    }

    public Metadata(@JsonProperty("id") String id, @JsonProperty("type") String type, @JsonProperty("rating") double rating, @JsonProperty("reviews") int reviews) {
        this.id = id;
        this.type = type;
        this.rating = rating;
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }
}
