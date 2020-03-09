package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

}
