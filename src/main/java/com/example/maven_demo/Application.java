package com.example.maven_demo;

import com.example.maven_demo.manager.BookManager;
import com.example.maven_demo.manager.UserManager;
import com.example.maven_demo.manager.impl.BookManagerImpl;
import com.example.maven_demo.manager.impl.UserManagerImpl;
import com.example.maven_demo.models.Book;
import com.example.maven_demo.models.User;
import com.example.maven_demo.models.enums.Gender;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Application {

    private User currentUser;

    private final static Scanner scanner = new Scanner(System.in);

    private final UserManager userManager = new UserManagerImpl();
    private final BookManager bookManager = new BookManagerImpl();

    public void start() {
        welcomePage();
        String command = scanner.nextLine();
        switch (command) {
            case "0": {
                exit();
                break;
            }
            case "1": {
                login();
                break;
            }
            case "2": {
                register();
                break;
            }
            default: {
            }
        }


    }

    private void register() {
        System.out.println("Input your email");
        String email = scanner.nextLine();
        while (userManager.existByEmail(email)) {
            System.out.println("Email already used");
            System.out.println("Input your email");
            email = scanner.nextLine();
        }

        System.out.println("Input your name");
        String name = scanner.nextLine();

        System.out.println("Input your surname");
        String surname = scanner.nextLine();

        System.out.println("Input your password");
        String password = scanner.nextLine();

        System.out.println("Input your gender");
        String gender = scanner.nextLine();

        System.out.println("Input your age");
        int age = Integer.parseInt(scanner.nextLine());

        currentUser = userManager.save(User.builder()
                .name(name)
                .surname(surname)
                .email(email)
                .password(password)
                .gender(Gender.valueOf(gender))
                .age(age)
                .build());
        userHome();

    }

    private void login() {
        System.out.println("Input your email");
        String email = scanner.nextLine();

        System.out.println("Input your password");
        String password = scanner.nextLine();
        User currentUser = userManager.getByEmailAndPassword(email, password);
        if (currentUser == null) {
            System.out.println("Incorrect email or password");
            start();
        } else {
            this.currentUser = currentUser;
            userHome();

        }
    }

    private void userHome() {
        System.out.println("For logout press 1");
        System.out.println("For add new book press 2");
        System.out.println("For vew my books press 3");
        System.out.println("For vew all books press 4");
        String command = scanner.nextLine();
        switch (command) {
            case "1": {
                currentUser = null;
                start();
            }
            case "2": {
                addNewBook();
                break;
            }
            case "3": {
                booksByAuthor(currentUser);
                break;
            }
            case "4" : {
                allBooks();
                break;
            }
        }
    }

    private void allBooks() {
        List<Book> books = bookManager.allBooks();
        books.forEach(book -> {
            System.out.println(String.format("Book {name: %s, created: %s author: %s} ", book.getName(), book.getCreatedAt(), book.getAuthor()));
        });
        if(books.isEmpty()){
            System.out.println("There are no any books in storage");
        }
        userHome();
    }

    private void booksByAuthor(User author) {
        List<Book> booksByAuthor = bookManager.getBooksByAuthor(author);
        booksByAuthor.forEach(book -> {
            System.out.println(String.format("Book {name: %s, created: %s author: %s} ", book.getName(), book.getCreatedAt(), book.getAuthor()));
        });
        if(booksByAuthor.isEmpty()){
            System.out.println("There are no any books");
        }
        userHome();
    }

    private void addNewBook() {
        System.out.println("Please enter book name");
        String bookName =  scanner.nextLine();
        Book savedBook = bookManager.save(Book.builder()
                .author(currentUser)
                .authorId(currentUser.getId())
                .name(bookName)
                .createdAt(new Date())
                .build());
        System.out.println("Book successfully saved with id = "+savedBook.getId());
        userHome();

    }

    private void exit() {
        System.out.println("By ... ");
        System.exit(0);
    }

    private void welcomePage() {
        System.out.println("For exit press 0");
        System.out.println("For login press 1.");
        System.out.println("For registration press 2.");
    }
}
