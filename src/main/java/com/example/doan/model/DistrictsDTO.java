package com.example.doan.model;

import lombok.Data;

@Data
public class DistrictsDTO {
    private long id;

    private String name;

    public DistrictsDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
