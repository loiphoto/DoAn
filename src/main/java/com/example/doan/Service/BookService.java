package com.example.doan.Service;

import com.example.doan.Entity.Book;
import com.example.doan.Entity.Category;
import com.example.doan.Repository.BookRepository;
import com.example.doan.exception.BookNotFoundException;
import com.example.doan.model.BookSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public BookSaveRequest getBookById(Long id) throws BookNotFoundException {
        Optional<Book> book = bookRepository.findById(id);

        BookSaveRequest bookSaveRequest = new BookSaveRequest();
        bookSaveRequest.setId(book.get().getId());
        bookSaveRequest.setName(book.get().getName());
        bookSaveRequest.setCategoryId(book.get().getCategory().getId());
        bookSaveRequest.setPrice(book.get().getPrice());
        bookSaveRequest.setQuantity(book.get().getQuantity());
        bookSaveRequest.setAuthor(book.get().getAuthor());
        bookSaveRequest.setPublicationDate(book.get().getPublicationDate());
        bookSaveRequest.setSize(book.get().getSize());
        bookSaveRequest.setTranslator(book.get().getTranslator());
        bookSaveRequest.setPublisher(book.get().getPublisher());
        bookSaveRequest.setNumberOfPage(book.get().getNumberOfPage());
        bookSaveRequest.setPurchasePrice(book.get().getPurchasePrice());
        bookSaveRequest.setDescription(book.get().getDescription());
        bookSaveRequest.setImage(book.get().getImage());

        if (book.isPresent()) {
            return bookSaveRequest;
        }
        throw new BookNotFoundException("Could not find any books with ID" + id);
    }

    public void deleteById(Long id) throws BookNotFoundException {
        Long count = bookRepository.countById(id);
        if (count == null || count == 0) {
            throw new BookNotFoundException("Could not find any books with ID" + id);
        }

        bookRepository.deleteById(id);
    }

    public List<Book> findAllByCategory(Optional<Category> category) {
        return bookRepository.findAllByCategory(category);
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Page<Book> findAllByCategory(Optional<Category> category, int pageNum) {
        int pageSize = 8;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        return bookRepository.findAllByCategory(category, pageable);
    }

    public Page<Book> findAllByCategorySortLatest(Optional<Category> category, int pageNum) {
        int pageSize = 8;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("publicationDate").descending());

        return bookRepository.findAllByCategory(category, pageable);
    }

    public Page<Book> findAllByCategorySortPriceAsc(Optional<Category> category, int pageNum) {
        int pageSize = 8;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("price"));

        return bookRepository.findAllByCategory(category, pageable);
    }

    public Page<Book> findAllByCategorySortPriceDes(Optional<Category> category, int pageNum) {
        int pageSize = 8;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("price").descending());

        return bookRepository.findAllByCategory(category, pageable);
    }


    public List<Book> findAllByKeyword(String keyword) {
        return bookRepository.findAllByKeyword(keyword);
    }
}
