package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Category {
    @Id
    @JsonProperty("name")
    private String name;
    @JsonProperty("hasLength")
    private boolean length;

    public Category(){}

    public Category(String name, boolean flag) {
        this.name = name;
        this.length = flag;
    }

    public String getName() {
        return name;
    }

    public boolean hasLength() {
        return length;
    }

    public void setHasLength(boolean flag) {
        this.length = flag;
    }
}
