package com.noirix.repository.impl;

import com.noirix.domain.User;
import com.noirix.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    public static final String DATABASE_URL = "jdbc:postgresql://localhost:";
    public static final int DATABASE_PORT = 5432;
    public static final String DATABASE_NAME = "/webinar_database";
    public static final String DATABASE_LOGIN = "postgres";
    public static final String DATABASE_PASSWORD = "postgres";

    @Override
    public List<User> search(String query) {

        return null;
    }

    @Override
    public User save(User object) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(Long key) {
        return null;
    }

    @Override
    public Optional<User> findOne(Long key) {
        return Optional.empty();
    }

    @Override
    public User update(User object) {
        return null;
    }

    @Override
    public Long delete(User object) {
        return null;
    }
}
