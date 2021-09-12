package com.example.doan.Controller;

import com.example.doan.Entity.*;
import com.example.doan.Repository.OrderItemRepository;
import com.example.doan.Repository.ProvincesRepository;
import com.example.doan.Service.*;
import com.example.doan.exception.UserNotFoundException;
import com.example.doan.model.OrderRequest;
import com.example.doan.model.UserOrderSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProvincesRepository provincesRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/cart")
    public ModelAndView cart() {
        ModelAndView mav = new ModelAndView("cart");
        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);
//        System.out.println(user);

        List<CartItem> cartItems = cartItemService.listCartItems(user);
        mav.addObject("cartItems", cartItems);

        for (CartItem cartItem : cartItems) {
            cartItem.getBook();
        }
        System.out.println(cartItems);
        return mav;
    }


    @PostMapping("/add-to-cart/{bookId}/{qty}")
    @ResponseBody
    public String addBookToCart(@PathVariable("bookId") Long bookId,
                                @PathVariable("qty") Integer quantity) {

        System.out.println("add Book to Cart: " + bookId + " - " + quantity);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);
        System.out.println(user);
        if (user == null)
            return "You must login to add this book to your cart";

        Integer addedQuantity = cartItemService.addBook(bookId, quantity, user);

        if (addedQuantity == -1)
            return "Không đủ số lượng trong kho hàng";
        return addedQuantity + " item(s) of this book were added to your shopping cart.";
    }

    @PostMapping("/cart/update/{bookId}/{qty}")
    @ResponseBody
    public String updateQuantity(@PathVariable("bookId") Long bookId,
                                 @PathVariable("qty") Integer quantity) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);

        int subTotal = cartItemService.updateQuantity(bookId, quantity, user);

        if (subTotal == -1)
            return null;
        return String.valueOf(subTotal);
    }

    @PostMapping("/cart/remove/{bookId}")
    @ResponseBody
    public String removeBookFromCart(@PathVariable("bookId") Long bookId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);
        System.out.println(user);
        if (user == null)
            return "You must login to remove this book from your cart.";
        cartItemService.removeProduct(bookId, user);

        return "The book has been removed from your shopping cart.";
    }


    @GetMapping("/cart/check-out")
    public ModelAndView checkout() {
        ModelAndView mav = new ModelAndView("checkOut");

        mav.addObject("orderRequest", new OrderRequest());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);
        UserOrderSave userOrderSave = new UserOrderSave(user);
        mav.addObject("user", userOrderSave);

        List<Provinces> provincesList = provincesRepository.findAll();
        mav.addObject("provincesList", provincesList);

        return mav;
    }

    @PostMapping("/order/create")
    public ModelAndView createOrder(@ModelAttribute("user") UserOrderSave userOrderSave, @ModelAttribute("orderRequest") OrderRequest orderRequest) throws UserNotFoundException, MessagingException {
        ModelAndView mav = new ModelAndView("redirect:/");
        Long id = orderService.createOrder(orderRequest, userOrderSave);

        String from = "loiphoto19111999@gmail.com";
        String to = "hoangvannam1999tb@gmail.com";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("Đặt hàng thành công");

        Order order = orderService.getOrder(id);
        String content = "Mã đơn hàng + " + order.getId() + "</br>";

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(id);
        System.out.println(orderItems);
        for (OrderItem item: orderItems){
            StringBuilder text = new StringBuilder("");
            text.append("Tên hàng: " + item.getBook().getName()).append("</br>")
                    .append("Số lượng: ").append(item.getQuantity()).append("</br>")
                    .append("Đơn giá: ").append(item.getPrice()).append("</br>");
            content += text;
        }
        content= content + "Phí vận chuyển: "+ order.getTransport_fee()+"</br>"+"Tổng tiền: " + order.getTotal();

        message.setText(content);

        mailSender.send(message);
        System.out.println(message);

        return mav;
    }

}
