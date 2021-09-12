package com.example.doan.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String name;

    @ManyToOne(optional = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Collection<Order> orderList;

    @OneToMany(mappedBy = "user")
    private Collection<CartItem> cartItems;

    @ManyToOne
    private Wards ward;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", role=" + role +
                ", orderList=" + orderList +
                ", cartItems=" + cartItems +
                ", ward=" + ward +
                '}';
    }
}
