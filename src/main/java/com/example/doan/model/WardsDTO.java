package com.example.doan.model;

import lombok.Data;

@Data
public class WardsDTO {
    private long id;

    private String name;

    private int transport_fee;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTransport_fee() {
        return transport_fee;
    }

    public void setTransport_fee(int transport_fee) {
        this.transport_fee = transport_fee;
    }

    public WardsDTO(long id, String name, int transport_fee) {
        this.id = id;
        this.name = name;
        this.transport_fee = transport_fee;
    }
}
