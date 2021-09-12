package com.example.doan.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Wards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private int transport_fee;

    @Column
    private String type;

    @OneToMany(mappedBy = "ward")
    private Collection<User> userList;

    @ManyToOne(optional = false)
    private Districts district;

}
