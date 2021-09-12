package com.example.doan.Repository;

import com.example.doan.Entity.Book;
import com.example.doan.Entity.CartItem;
import com.example.doan.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    CartItem findByUserAndBook(User user, Book book);

    @Query("UPDATE CartItem c SET c.quantity=?1 where c.book.id=?2 and c.user.id=?3")
    @Modifying
    void updateQuantity(Integer quantity, Long bookId, Long userId);

    @Query("delete from CartItem c where c.user.id=?1 and c.book.id=?2")
    @Modifying
    void deleteByUserAndBook(Long userId, Long bookId);

    @Query("delete from CartItem c where c.user.id=?1")
    @Modifying
    @Transactional
    void deleteByUser(Long userId);
}
