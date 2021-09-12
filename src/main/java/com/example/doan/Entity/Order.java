package com.example.doan.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity(name = "Orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int status;

    @Column
    private LocalDateTime dateTime;

    @Column
    private int total;

    @Column
    private String paymentMethod;

    @Column
    private String note;

    @Column
    private int transport_fee;

    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private Collection<OrderItem> orderItems;

    @ManyToOne
    private User user;

}
