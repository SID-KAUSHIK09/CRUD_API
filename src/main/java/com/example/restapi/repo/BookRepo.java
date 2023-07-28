package com.example.restapi.repo;

import com.example.restapi.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book,Long> {
    Page<Book> findByTitleContaining(String title, Pageable pageable);
}
