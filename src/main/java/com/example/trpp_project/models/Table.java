package com.example.trpp_project.models;


import com.example.trpp_project.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@javax.persistence.Table(name = "tables")
@Data
public class Table implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @NotEmpty
    private String name;

    @Min(0)
    private int position;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;


    @JsonIgnore
    private long timestamp;

    public Table() {
    }

    public Table( String name,  int pos) {
        this.name = name;
        this.position = pos;
    }
    @JsonView(Views.get.class)
    public int getId() {
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

    @JsonView(Views.get.class)
    public int getPosition() {
        return position;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonView(Views.internal.class)
    private List<Card> cards;

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pos=" + position +
                ", timestamp=" + timestamp +
                '}';
    }



     public Table clone() throws CloneNotSupportedException {
        return (Table) super.clone();
    }
}
