package com.example.doan.Controller;

import com.example.doan.Entity.Book;
import com.example.doan.Entity.Category;
import com.example.doan.Entity.Provinces;
import com.example.doan.Entity.User;
import com.example.doan.Repository.ProvincesRepository;
import com.example.doan.Repository.RoleRepository;
import com.example.doan.Repository.WardsRepository;
import com.example.doan.Service.BookService;
import com.example.doan.Service.CategoryService;
import com.example.doan.Service.UserService;
import com.example.doan.exception.UserAlreadyExistException;
import com.example.doan.model.UserLogin;
import com.example.doan.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProvincesRepository provincesRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WardsRepository wardsRepository;

    @GetMapping("/login")
    public ModelAndView viewLoginPage() {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @PostMapping("/fail_login")
    public String handleFailedLogin(Model model){
        System.out.println("A user has failed to login");
        return "redirect:/login?error";
    }

    @GetMapping("/sign-up")
    public ModelAndView viewSignUp() {
        ModelAndView mav = new ModelAndView("signUp");
        UserLogin userLogin = new UserLogin();
        mav.addObject("user", userLogin);
        return mav;
    }

    @PostMapping("/sign-up/check-email/{email}")
    @ResponseBody
    public Boolean checkEmail(@PathVariable(name = "email") String email) {
        System.out.println(email);
        return userService.emailExists(email);
    }

    @PostMapping("/sign-up/create")
    public ModelAndView signUp(@ModelAttribute("user") UserLogin userLogin) throws UserAlreadyExistException {
        ModelAndView mav = new ModelAndView("redirect:/login");
        userService.registerNewUserAccount(userLogin);
        return mav;
    }

    @GetMapping({"/", "/home"})
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("index");
        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);
        List<Book> bookList = bookService.findAll();
        mav.addObject("bookList", bookList);
        Optional<Category> category = categoryService.findCategoryByName("Văn học");
        List<Book> bookListVanHoc = bookService.findAllByCategory(category);
        mav.addObject("bookListVanHoc", bookListVanHoc);
        System.out.println(category);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);
//        System.out.println(user);

        return mav;
    }

    @GetMapping("/book/view/{id}")
    public ModelAndView singleBook(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("singleBook");

        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);

        Optional<Book> book = bookService.findBookById(id);
//        System.out.println(book);
        mav.addObject("book", book.get());
        mav.addObject("bookId", book.get().getId());
        return mav;
    }

    @GetMapping("/category/{id}/page/{pageNum}")
    public ModelAndView viewBookByCategory(@PathVariable("id") Long id, @PathVariable(name = "pageNum")  int pageNum) {
        ModelAndView mav = new ModelAndView("category");

        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);

        Long categoryId = categoryService.findById(id).get().getId();
        mav.addObject("categoryId", categoryId);


        Page<Book> page = bookService.findAllByCategory(categoryService.findById(id), pageNum);
        List<Book> bookList = page.getContent();

        mav.addObject("currentPage", pageNum);
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("totalItems", page.getTotalElements());
        mav.addObject("bookList", bookList);

        return mav;
    }

    @GetMapping("/category/{id}/page/latest/{pageNum}")
    public ModelAndView viewBookByCategorySort(@PathVariable("id") Long id, @PathVariable(name = "pageNum")  int pageNum) {
        ModelAndView mav = new ModelAndView("category");

        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);

        Long categoryId = categoryService.findById(id).get().getId();
        mav.addObject("categoryId", categoryId);

        Page<Book> page = bookService.findAllByCategorySortLatest(categoryService.findById(id), pageNum);
        List<Book> bookList = page.getContent();

        mav.addObject("currentPage", pageNum);
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("totalItems", page.getTotalElements());
        mav.addObject("bookList", bookList);

        return mav;
    }

    @GetMapping("/category/{id}/page/price-asc/{pageNum}")
    public ModelAndView viewBookByCategorySortPriceAsc(@PathVariable("id") Long id, @PathVariable(name = "pageNum")  int pageNum) {
        ModelAndView mav = new ModelAndView("category");

        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);

        Long categoryId = categoryService.findById(id).get().getId();
        mav.addObject("categoryId", categoryId);

        Page<Book> page = bookService.findAllByCategorySortPriceAsc(categoryService.findById(id), pageNum);
        List<Book> bookList = page.getContent();

        mav.addObject("currentPage", pageNum);
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("totalItems", page.getTotalElements());
        mav.addObject("bookList", bookList);

        return mav;
    }

    @GetMapping("/category/{id}/page/price-des/{pageNum}")
    public ModelAndView viewBookByCategorySortPriceDes(@PathVariable("id") Long id, @PathVariable(name = "pageNum")  int pageNum) {
        ModelAndView mav = new ModelAndView("category");

        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);

        Long categoryId = categoryService.findById(id).get().getId();
        mav.addObject("categoryId", categoryId);

        Page<Book> page = bookService.findAllByCategorySortPriceDes(categoryService.findById(id), pageNum);
        List<Book> bookList = page.getContent();

        mav.addObject("currentPage", pageNum);
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("totalItems", page.getTotalElements());
        mav.addObject("bookList", bookList);

        return mav;
    }

    @GetMapping("/search")
    public ModelAndView findAllByKeyword(@Param("keyword")  String keyword) {
        ModelAndView mav = new ModelAndView("search");

        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);
        List<Book> bookList = bookService.findAllByKeyword(keyword);
        mav.addObject("bookList", bookList);

        return mav;
    }

    @GetMapping("/user/account/profile")
    public ModelAndView viewProfile(){
        ModelAndView mav = new ModelAndView("profileUser");

        List<Category> categoryList = categoryService.findAll();
        mav.addObject("categoryList", categoryList);

        List<Provinces> provincesList = provincesRepository.findAll();
        mav.addObject("provincesList", provincesList);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userCurrent = userService.getCurrentlyLoggedInUser(authentication);
        UserProfile user = new UserProfile(userCurrent);
        mav.addObject("user", user);
        System.out.println(user);

        return mav;
    }

    @PostMapping("/user/save")
    public ModelAndView saveUser(@ModelAttribute("user") @Valid UserProfile userProfile){
        ModelAndView mav = new ModelAndView("redirect:/");
        System.out.println(userProfile);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(authentication);

        user.setName(userProfile.getName());
        user.setPhone(userProfile.getPhone());
        user.setAddress(userProfile.getAddress());
        user.setWard(wardsRepository.getById(userProfile.getWardId()));

        userService.save(user);
        return mav;
    }
    
}
