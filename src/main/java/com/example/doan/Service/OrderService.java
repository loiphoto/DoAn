package com.example.doan.Service;

import com.example.doan.Entity.*;
import com.example.doan.Repository.CartItemRepository;
import com.example.doan.Repository.OrderItemRepository;
import com.example.doan.Repository.OrderRepository;
import com.example.doan.Repository.WardsRepository;
import com.example.doan.exception.UserNotFoundException;
import com.example.doan.model.OrderRequest;
import com.example.doan.model.UserOrderSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WardsRepository wardsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public long createOrder(OrderRequest orderRequest, UserOrderSave userOrderSave) throws UserNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);
        user.setName(userOrderSave.getName());
        user.setWard(wardsRepository.getById(userOrderSave.getWardId()));
        user.setAddress(userOrderSave.getAddress());
        user.setPhone(userOrderSave.getPhone());
        userService.save(user);

        Order order = new Order();
        order.setUser(user);
        order.setTotal(orderRequest.getTotal());
        order.setStatus(1);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setDateTime(LocalDateTime.now());
        order.setNote(orderRequest.getNote());
        order.setTransport_fee(orderRequest.getTransport_fee());

        orderRepository.save(order);

        List<CartItem> cartItemList = (List<CartItem>) user.getCartItems();
        List<OrderItem> orderItemList = cartItemList.stream().map(cartItem -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(cartItem.getBook());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getBook().getPrice());
            return item;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItemList);

        cartItemRepository.deleteByUser(user.getId());
        return order.getId();
    }

    public void updateStatusOrder(Long id){
        Order order = orderRepository.findById(id).orElse(null);
        System.out.println(order.getStatus());
        if (order == null ||order.getStatus()==3){
            return;
        }
        else {
            order.setStatus(order.getStatus()+1);
            if (order.getStatus() == 3) {
                order.getOrderItems().stream().map(orderItem -> {
                    orderItem.getBook().setQuantity(orderItem.getBook().getQuantity()-orderItem.getQuantity());
                    return orderItem.getBook();
                }).collect(Collectors.toList());
            }
            System.out.println("Sau khi cap nhat " + order.getStatus());
        }
        orderRepository.save(order);
    }

    public List<OrderItem> viewOrderDetails(Long id){
        Optional<Order> order = orderRepository.findById(id);
        return (List<OrderItem>) order.get().getOrderItems();
    }

    public void deleteOrder(Long id){
        Order order = orderRepository.findById(id).orElse(null);
        orderRepository.delete(order);
    }

    public Order getOrder(Long id){
        return orderRepository.getById(id);
    }
}
