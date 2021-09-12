package com.example.doan.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long  id;

    @Column
    private String name;

    @Column
    private String image;

    @Column
    private int price;

    @Column
    private int quantity;

    @Column
    private String author;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @Column
    private String size;

    @Column
    private String translator;

    @Column
    private String publisher;

    @Column
    private int numberOfPage;

    @Column
    private double purchasePrice;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @ManyToOne(optional = false)
    private Category category;

    @OneToMany(mappedBy = "book")
    private Collection<CartItem> cartItems;

    @OneToMany(mappedBy = "book")
    private Collection<OrderItem> orderItems;

    @Transient
    public String getBookImage(){
        if(image == null || id==null) return null;

        return "/images/"+image;
    }

    @ManyToMany
    private Collection<Blog> blog_id;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", author='" + author + '\'' +
                ", publicationDate=" + publicationDate +
                ", size='" + size + '\'' +
                ", translator='" + translator + '\'' +
                ", publisher='" + publisher + '\'' +
                ", numberOfPage=" + numberOfPage +
                ", purchasePrice=" + purchasePrice +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", cartItems=" + cartItems +
                ", orderItems=" + orderItems +
                ", blog_id=" + blog_id +
                '}';
    }
}
