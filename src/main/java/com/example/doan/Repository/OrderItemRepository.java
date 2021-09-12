package com.example.doan.Repository;

import com.example.doan.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select o from OrderItem o where o.order.id=?1 ")
    List<OrderItem> findAllByOrderId(Long id);

}
