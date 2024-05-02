package com.kieran.csvdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cat_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cat {

    @Id
    private String id;
    private String name;
    private String breed;
    private String color;
    private String age;
}
