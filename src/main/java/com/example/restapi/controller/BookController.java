package com.example.restapi.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.restapi.model.Book;
import com.example.restapi.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.Optional;
@Controller

public class BookController {
    @Autowired
    private BookRepo bookRepo;



    @GetMapping("/getAllBooks")
    public ResponseEntity<Page<Book>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(required = false) String title) {
        try {
            Pageable pageable;
            if (title != null) {
                pageable = PageRequest.of(page, size, Sort.by("title").ascending());
                Page<Book> bookPage = bookRepo.findByTitleContaining(title, pageable);
                if (bookPage.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(bookPage, HttpStatus.OK);
            } else {
                pageable = PageRequest.of(page, size);
                Page<Book> bookPage = bookRepo.findAll(pageable);
                if (bookPage.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(bookPage, HttpStatus.OK);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBooksById/{id}")
    public ResponseEntity <Book> getBookById(@PathVariable Long id){
        Optional <Book> bookData=bookRepo.findById(id);
        if (bookData.isPresent()){
            return new ResponseEntity<>(bookData.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/addBookForm")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/addBook")
    public String addBook( @ModelAttribute("book")  Book book) {
        bookRepo.save(book);
        return "redirect:/getAllBooks";
    }


    @PostMapping("/updateBookById/{id}")
    public ResponseEntity <Book> updateBookById(@PathVariable Long id, @RequestBody Book newbookdata){
        Optional <Book> oldbookdata=bookRepo.findById(id);
        if (oldbookdata.isPresent()){
            Book updatedbookdata=oldbookdata.get();
            updatedbookdata.setTitle(newbookdata.getTitle());
            updatedbookdata.setAuthor(newbookdata.getAuthor());

            Book bookobj=bookRepo.save(updatedbookdata);
            return new ResponseEntity<>(bookobj,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id){
        bookRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
