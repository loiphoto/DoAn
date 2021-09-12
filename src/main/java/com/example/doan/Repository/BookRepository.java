package com.example.doan.Repository;

import com.example.doan.Entity.Book;
import com.example.doan.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Long countById(Long id);

    List<Book> findAllByCategory(Optional<Category> category);

    Page<Book> findAllByCategory(Optional<Category> category,Pageable pageable);

    @Query("select b from Book b where "+ "concat(b.name, b.author, b.publisher, b.translator) like %?1%")
    List<Book> findAllByKeyword(String keyword);

}
