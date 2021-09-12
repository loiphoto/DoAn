package com.example.doan.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class BookSaveRequest {
    private Long  id;

    private String name;

    private long categoryId;

    private int price;

    private int quantity;

    private String author;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    private String size;

    private String translator;

    private String publisher;

    private int numberOfPage;

    private double purchasePrice;

    private String description;

    private String image;
}
