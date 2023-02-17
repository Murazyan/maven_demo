package com.example.maven_demo.manager.impl;

import com.example.maven_demo.manager.BookManager;
import com.example.maven_demo.mapper.UserMapper;
import com.example.maven_demo.models.Book;
import com.example.maven_demo.models.User;
import com.example.maven_demo.provider.DBConnectionProvider;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookManagerImpl implements BookManager {
    private final UserMapper userMapper = new UserMapper();
    private final DBConnectionProvider provider = DBConnectionProvider.getInstance();

    @Override
    @SneakyThrows
    public Book save(Book book) {
        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO books (name, created_at, author_id) VALUES (?,?,?)";
        try {

            preparedStatement = provider.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setDate(2, new java.sql.Date(book.getCreatedAt().getTime()));
            preparedStatement.setInt(3, book.getAuthorId());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
        return book;
    }

    @Override
    public Book getById(int id) {
        return null;
    }

    @Override
    @SneakyThrows
    public List<Book> allBooks() {
        List<Book> books =  new ArrayList<>();
        Statement statement = provider.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT " +
                "  b.`id` AS book_id, " +
                "  b.`author_id` AS author_id," +
                "  b.`name` AS book_name," +
                "  b.`created_at` AS created_at," +
                "   u.id, " +
                "  u.name, " +
                "  u.surname, " +
                "  u.email, " +
                "  u.gender, " +
                "  u.age  " +
                "FROM books b  " +
                "  INNER JOIN users u ON b.`author_id` = u.`id`");
        while (resultSet.next()){
            books.add(Book.builder()
                            .id(resultSet.getInt("book_id"))
                            .authorId(resultSet.getInt("author_id"))
                            .name(resultSet.getString("book_name"))
                            .createdAt(resultSet.getDate("created_at"))
                            .author(userMapper.apply(resultSet))
                    .build());
        }
        return  books;
    }

    @Override
    @SneakyThrows
    public List<Book> getBooksByAuthor(User author) {
        List<Book> books = new ArrayList<>();
        PreparedStatement statement = provider.getConnection()
                .prepareStatement("select * from books b where b.author_id = ?");
        statement.setInt(1, author.getId());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            books.add(Book.builder()
                            .authorId(author.getId())
                            .author(author)
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .createdAt(resultSet.getDate("created_at"))
                    .build());
        }
        return books;

    }

    @Override
    public void update(Book book) {

    }
}
