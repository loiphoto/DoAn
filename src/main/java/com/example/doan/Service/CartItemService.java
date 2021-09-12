package com.example.doan.Service;

import com.example.doan.Entity.Book;
import com.example.doan.Entity.CartItem;
import com.example.doan.Entity.User;
import com.example.doan.Repository.BookRepository;
import com.example.doan.Repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<CartItem> listCartItems(User user){
        return cartItemRepository.findByUser(user);
    }

    public Integer addBook(Long bookId, Integer quantity, User user){
        Integer addedQuantity = quantity;

        Book book = bookRepository.findById(bookId).get();
        CartItem cartItem = cartItemRepository.findByUserAndBook(user, book);

        if (cartItem!=null){
            addedQuantity = cartItem.getQuantity() + quantity;
        }
        else {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setBook(book);
        }
        System.out.println("Số lượng thêm: " + addedQuantity);
        if (addedQuantity> bookRepository.findById(bookId).get().getQuantity()){
            return -1;
        }
        cartItem.setQuantity(addedQuantity);
        cartItemRepository.save(cartItem);
        return addedQuantity;
    }

    public int updateQuantity(Long bookId, Integer quantity, User user){
        System.out.println("update quantity: "+ bookId + " - " + quantity + " - " + user.getId());

        if (quantity>bookRepository.findById(bookId).get().getQuantity()){
            return -1;
        }
        cartItemRepository.updateQuantity(quantity, bookId, user.getId());
        Book book = bookRepository.findById(bookId).get();
        int subTotal = book.getPrice()* quantity;
        return subTotal;
    }

    public void removeProduct(Long bookId, User user){
        cartItemRepository.deleteByUserAndBook(user.getId(), bookId);
    }
}
