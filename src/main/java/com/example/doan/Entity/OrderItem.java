package com.example.doan.Entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int quantity;

    @Column
    private double price;

    @ManyToOne
    private Book book;

    @ManyToOne(optional = false)
    private Order order;

}
