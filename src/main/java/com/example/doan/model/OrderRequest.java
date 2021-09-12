package com.example.doan.model;

import com.example.doan.Entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRequest {

    private int status;

    private LocalDateTime dateTime;

    private int transport_fee;

    private int total;

    private String paymentMethod;

    private String note;
}
