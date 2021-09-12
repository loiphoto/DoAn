package com.example.doan.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Districts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String type;

    @OneToMany(mappedBy = "district")
    private Collection<Wards> wardList;

    @ManyToOne(optional = false)
    private Provinces provinces;


}
