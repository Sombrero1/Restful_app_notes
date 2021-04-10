package com.example.trpp_project.models;

import com.example.trpp_project.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Min;

public class Card {
    int id;
    @NotEmpty
    String name;
    @Min(1)
    int numberOfList;
    @Min(1)
    int pos;

    @JsonIgnore
    long timestamp;

    public Card(int id, String name,  int numberOfList, int pos) {
        this.id = id;
        this.name = name;
        this.pos = pos;
        this.numberOfList = numberOfList;
    }
    public int getId() {
        return id;
    }

    @JsonView(Views.get.class)
    public void setId(int id) {
        this.id = id;
    }

    @JsonView(Views.get.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonView(Views.get.class)
    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumberOfList() {
        return numberOfList;
    }

    public void setNumberOfList(int numberOfList) {
        this.numberOfList = numberOfList;
    }
}
