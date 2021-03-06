package com.example.trpp_project.models;


import com.example.trpp_project.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Min;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotEmpty
    private String name;


    @Min(0)
    private int position;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;


    @JsonIgnore
    private long timestamp;

    public Card() {
    }

    public Card( String name, int numberOfList, int pos) {
        this.name = name;
        this.position = pos;

    }
    @JsonView(Views.get.class)
    public long getId() {
        return id;
    }

//    @JsonView(Views.get.class)
//    public void setId(int id) {
//        this.id = id;
//    }

    @JsonView(Views.get.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonView(Views.get.class)
    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pos=" + position +
                ", timestamp=" + timestamp +
                '}';
    }



}
