package com.example.doan.Controller.Admin;

import com.example.doan.Entity.*;
import com.example.doan.Repository.RoleRepository;
import com.example.doan.Service.BookService;
import com.example.doan.Service.CategoryService;
import com.example.doan.Service.OrderService;
import com.example.doan.Service.UserService;
import com.example.doan.exception.BookNotFoundException;
import com.example.doan.exception.CategoryNotFoundException;
import com.example.doan.exception.UserNotFoundException;
import com.example.doan.model.BookSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OrderService orderService;


    @GetMapping()
    public ModelAndView adminHome() {
        ModelAndView mav = new ModelAndView("adminIndex");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            System.out.println(((UserDetails) principal).getAuthorities());
            System.out.println(username);
        } else {
            String username = principal.toString();
            System.out.println(username);
        }
        int totalBooks = bookService.findAll().size();
        mav.addObject("totalBooks", totalBooks);
        int totalUsers = userService.findAll().size();
        mav.addObject("totalUsers", totalUsers);
        int totalOrders = orderService.findAll().size();
        mav.addObject("totalOrders", totalOrders);
        int totalRevenue = orderService.findAll().stream().mapToInt(Order::getTotal).sum();
        mav.addObject("totalRevenue", totalRevenue);
        return mav;
    }

    //    Users
    @GetMapping("/users")
    public ModelAndView viewUserList() {
        ModelAndView mav = new ModelAndView("adminUsers");
        List<User> listUsers = userService.findAll();
        mav.addObject("listUsers", listUsers);
        return mav;
    }

    @GetMapping("/users/register")
    public ModelAndView showRegisterUsersForm() {
        ModelAndView mav = new ModelAndView("adminUsersForm");
        mav.addObject("user", new User());
        mav.addObject("pageTitle", "Register User");
        return mav;
    }

    @PostMapping("/users/save")
    public ModelAndView processRegistration(User user, RedirectAttributes ra) {
        ModelAndView mav = new ModelAndView("redirect:/admin/users");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(roleRepository.getById(2l));
        if (userService.emailExists(user.getEmail())) {
            ra.addFlashAttribute("message", "The email already exists.");
        } else {
            userService.save(user);
            ra.addFlashAttribute("message", "The user has been saved successfully.");
        }
//        System.out.println(user);

        return mav;
    }

    @GetMapping("/users/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            ModelAndView mav = new ModelAndView("adminUsersForm");
            User user = userService.getUserById(id);
            System.out.println(user);
            mav.addObject("user", user);
            mav.addObject("pageTitle", "Edit User (ID: " + id + ")");
            return mav;
        } catch (UserNotFoundException e) {
            ModelAndView mav = new ModelAndView("redirect:/admin/users");
            ra.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
            return mav;
        }
    }

    @GetMapping("/users/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        ModelAndView mav = new ModelAndView("redirect:/admin/users");
        try {
            userService.deleteById(id);
            ra.addFlashAttribute("message", "The user ID " + id + " has been deleted.");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }
        return mav;
    }

    //    Category
    @GetMapping("/categories")
    public ModelAndView viewCategoriesList() {
        ModelAndView mav = new ModelAndView("adminCategories");
        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);
        return mav;
    }

    @GetMapping("/categories/create")
    public ModelAndView showCreateCategoriesForm() {
        ModelAndView mav = new ModelAndView("adminCategoriesForm");
        mav.addObject("category", new Category());
        mav.addObject("pageTitle", "Create Category");
        return mav;
    }


    @PostMapping("/categories/save")
    public ModelAndView processSaveCategory(Category category, RedirectAttributes ra) {
        ModelAndView mav = new ModelAndView("redirect:/admin/categories");
        categoryService.save(category);
        ra.addFlashAttribute("message", "The category has been saved successfully.");
        return mav;
    }

    @GetMapping("/categories/edit/{id}")
    public ModelAndView editCategory(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            ModelAndView mav = new ModelAndView("adminCategoriesForm");
            Category category = categoryService.getCategoryById(id);
            System.out.println(category);
            mav.addObject("category", category);
            mav.addObject("pageTitle", "Edit Category (ID: " + id + ")");
            return mav;
        } catch (CategoryNotFoundException e) {
            ModelAndView mav = new ModelAndView("redirect:/admin/categories");
            ra.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
            return mav;
        }
    }

    @GetMapping("/categories/delete/{id}")
    public ModelAndView deleteCategory(@PathVariable("id") Long id, RedirectAttributes ra) {
        ModelAndView mav = new ModelAndView("redirect:/admin/categories");
        try {
            categoryService.deleteById(id);
            ra.addFlashAttribute("message", "The Category ID " + id + " has been deleted.");
        } catch (CategoryNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }
        return mav;
    }

    //    Book
    @GetMapping("/books")
    public ModelAndView viewAllBook() {
        ModelAndView mav = new ModelAndView("adminBooks");
        List<Book> bookList = bookService.findAll();
        mav.addObject("bookList", bookList);
        return mav;
    }

    @GetMapping("/books/create")
    public ModelAndView showCreateForm() {
        ModelAndView mav = new ModelAndView("adminBooksForm");
        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);
        mav.addObject("bookSaveRequest", new BookSaveRequest());
        mav.addObject("pageTitle", "Create Book");
        return mav;
    }

    @PostMapping("/books/save")
    public ModelAndView saveBook(@ModelAttribute("bookSaveRequest") BookSaveRequest bookSaveRequest, RedirectAttributes ra, @RequestParam("imageFile") MultipartFile file) throws IOException, CategoryNotFoundException {
        ModelAndView mav = new ModelAndView("redirect:/admin/books");

        Book book = new Book();
        book.setId(bookSaveRequest.getId());
        book.setName(bookSaveRequest.getName());
        book.setCategory(categoryService.getCategoryById(bookSaveRequest.getCategoryId()));
        book.setPrice(bookSaveRequest.getPrice());
        book.setQuantity(bookSaveRequest.getQuantity());
        book.setAuthor(bookSaveRequest.getAuthor());
        book.setPublicationDate(bookSaveRequest.getPublicationDate());
        book.setSize(bookSaveRequest.getSize());
        book.setTranslator(bookSaveRequest.getTranslator());
        book.setPublisher(bookSaveRequest.getPublisher());
        book.setPurchasePrice(bookSaveRequest.getPurchasePrice());
        book.setNumberOfPage(bookSaveRequest.getNumberOfPage());
        book.setDescription(bookSaveRequest.getDescription());

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(bookSaveRequest.getPublicationDate());
        System.out.println(fileName);

        book.setImage(fileName);
        bookService.save(book);

        String uploadDir = "./src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir);

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        ra.addFlashAttribute("message", "The book has been saved successfully.");
        return mav;
    }


    @GetMapping("/books/edit/{id}")
    public ModelAndView editBook(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            ModelAndView mav = new ModelAndView("adminBooksForm");
            BookSaveRequest bookSaveRequest = bookService.getBookById(id);
            List<Category> categoryList = categoryService.findAll();

            mav.addObject("categoryList", categoryList);
            mav.addObject("bookSaveRequest", bookSaveRequest);

            mav.addObject("pageTitle", "Edit Book (ID: " + id + ")");
            return mav;
        } catch (BookNotFoundException e) {
            ModelAndView mav = new ModelAndView("redirect:/admin/books");
            ra.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
            return mav;
        }
    }

    @GetMapping("/books/delete/{id}")
    public ModelAndView deleteBook(@PathVariable("id") Long id, RedirectAttributes ra) {
        ModelAndView mav = new ModelAndView("redirect:/admin/books");
        try {
            bookService.deleteById(id);
            ra.addFlashAttribute("message", "The Book ID " + id + " has been deleted.");
        } catch (BookNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }
        return mav;
    }


    //    Order
    @GetMapping("/orders")
    public ModelAndView viewAllOrder() {
        ModelAndView mav = new ModelAndView("adminOrders");
        List<Order> orderList = orderService.findAll();
        mav.addObject("orderList", orderList);
        return mav;
    }

    @GetMapping("/orders/update/{id}")
    public ModelAndView updateStatusOrder(@PathVariable("id") Long id, RedirectAttributes ra) {
        ModelAndView mav = new ModelAndView("redirect:/admin/orders");
        orderService.updateStatusOrder(id);
        ra.addFlashAttribute("message", "The Order ID " + id + " update success!");
        System.out.println("Cập nhật thành công!");
        return mav;
    }

    @GetMapping("/orders/details/{id}")
    public ModelAndView viewDetailsOrder(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("orderDetails");
        List<OrderItem> orderItems = orderService.viewOrderDetails(id);
        mav.addObject("orderItems", orderItems);
        return mav;
    }

    @GetMapping("/orders/delete/{id}")
    public ModelAndView deleteOrder(@PathVariable("id") Long id, RedirectAttributes ra) {
        System.out.println("Test");
        ModelAndView mav = new ModelAndView("redirect:/admin/orders");
        orderService.deleteOrder(id);
        ra.addFlashAttribute("message", "The Order ID " + id + " update success!");

        return mav;
    }
}
