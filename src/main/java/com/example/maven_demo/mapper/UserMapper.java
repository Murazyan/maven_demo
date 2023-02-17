package com.example.maven_demo.mapper;

import com.example.maven_demo.models.User;
import com.example.maven_demo.models.enums.Gender;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class UserMapper implements Function<ResultSet, User> {

    @Override
    @SneakyThrows
    public User apply(ResultSet resultSet) {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setEmail(resultSet.getString("email"));
        user.setAge(resultSet.getInt("age"));
        user.setGender(Gender.valueOf(resultSet.getString("gender")));

        try {

            user.setPassword(resultSet.getString("password"));
        }catch (SQLException e){

        }

        return user;
    }
}
