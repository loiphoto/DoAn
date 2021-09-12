package com.example.doan.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String createBy;

    @Column
    private LocalDateTime createdDate;

    @Column
    private String title;

    @Column
    private String image;

    @Column(name = "content", columnDefinition="TEXT")
    private String content;

    @ManyToMany(mappedBy = "blog_id")
    private Collection<Book> book_id;

}
