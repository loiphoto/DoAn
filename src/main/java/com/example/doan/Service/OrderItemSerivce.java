package com.example.doan.Service;

import com.example.doan.Entity.Book;
import com.example.doan.Entity.Order;
import com.example.doan.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemSerivce {

    @Autowired
    private BookRepository bookRepository;


    public void addOrderItem(Long bookId, Order order){
        Book book = bookRepository.findById(bookId).get();
    }
}
