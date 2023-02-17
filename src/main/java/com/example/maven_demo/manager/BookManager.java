package com.example.maven_demo.manager;

import com.example.maven_demo.models.Book;
import com.example.maven_demo.models.User;

import java.util.List;

public interface BookManager {

    Book save(Book book);



    Book getById(int id);

    List<Book> allBooks();

    List<Book> getBooksByAuthor(User author);

    void update(Book book);
}
